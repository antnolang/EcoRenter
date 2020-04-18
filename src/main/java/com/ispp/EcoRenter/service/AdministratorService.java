package com.ispp.EcoRenter.service;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.ispp.EcoRenter.configuration.MyUserDetailsService;
import com.ispp.EcoRenter.form.AdminForm;
import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Administrator;
import com.ispp.EcoRenter.model.Comment;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.repository.AdministratorRepository;
import com.ispp.EcoRenter.security.UserAccount;

@Service
@Transactional
public class AdministratorService {

	// Repository --------------
	@Autowired
	private AdministratorRepository administratorRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private RentOutService rentoutService;

	@Autowired
	private RenterService renterService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private SmallholdingService smallholdingService;

	@Autowired
	private OwnerService ownerService;

	public AdministratorService() {
		super();
	}


	// CRUD methods ----------------------------
	public Administrator edit(AdminForm adminForm) {
		String name, surname, email, telephoneNumber, username, password;
		String passwordMatch, encodedPassword, usernameDB;
		UsernamePasswordAuthenticationToken usernameToken;
		Administrator result;
		UserAccount userAccount;
		Photo photo;
		MultipartFile file;
		UserDetails userDetails;

		result = this.findByPrincipal();

		name = adminForm.getName();
		surname = adminForm.getSurname();
		email = adminForm.getEmail();
		telephoneNumber = adminForm.getTelephoneNumber();
		username = adminForm.getUsername();
		password = adminForm.getPassword();
		passwordMatch = adminForm.getPasswordMatch();
		file = adminForm.getFile();

		userAccount = result.getUserAccount();
		// Las contraseñas deben coincidir.
		Assert.isTrue(this.actorService.checkPassword(password, passwordMatch), "Las contraseñas no coinciden.");

		usernameDB = result.getUserAccount().getUsername();
		// Si el usuario ha decidido cambiar de username, comprobar que no existe
		if (!usernameDB.equals(username)) {
			Assert.isTrue(this.actorService.checkNoRepeatedUsername(username), "El usuario elegido ya existe.");
		}

		// Insertar foto y setear Actor::photo.
		if (file != null) {
			photo = this.photoService.storeImage(file);

			if (photo != null) {
				result.setPhoto(photo);
			}

		}

		encodedPassword = this.passwordEncoder.encode(password);

		// Seteamos valores --------------------------
		userAccount.setUsername(username.trim());
		userAccount.setPassword(encodedPassword.trim());

		result.setName(name.trim());
		result.setSurname(surname.trim());
		result.setEmail(email.trim());
		result.setTelephoneNumber(telephoneNumber.trim());

		this.save(result);

		userDetails = this.myUserDetailsService.loadUserByUsername(username.trim());

		usernameToken = new UsernamePasswordAuthenticationToken(userDetails, null, userAccount.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(usernameToken);

		return result;
	}

	public void deleteRenter(Renter renter) {
		Collection<RentOut> rentsByThisRenter = this.rentoutService.findRentOutsByRenter(renter.getId());

		if(!rentsByThisRenter.isEmpty()) {
			for(RentOut r : rentsByThisRenter) {
				this.rentoutService.delete(r);
			}
		}

		Collection<Comment> commentsThisRenter = this.commentService.findCommentsByActor(renter.getId());

		if(!commentsThisRenter.isEmpty()) {
			for(Comment c : commentsThisRenter) {
				this.commentService.actorDelete(c);
			}
		}

		this.renterService.delete(renter);
	}


	public void deleteOwner(Owner owner) {
		Collection<Smallholding> smallByThisOwner = this.smallholdingService.findSmallholdingsByOwnerId(owner.getId());

		if(!smallByThisOwner.isEmpty()) {
			for(Smallholding s : smallByThisOwner) {

				Collection<RentOut> rentsForThisSmall = this.rentoutService.findBySmallholding(s.getId());

				if(!rentsForThisSmall.isEmpty()) {
					for(RentOut r : rentsForThisSmall) {
						this.rentoutService.delete(r);
					}
				}

				Collection<Comment> commentsByOwner = this.commentService.findCommentsBySmallholdingId(s.getId());

				if(!commentsByOwner.isEmpty()) {
					for(Comment c : commentsByOwner) {
						this.commentService.actorDelete(c);
					}
				}


				this.smallholdingService.delete(s);

			}
		}


		this.ownerService.delete(owner);
	}


	// Other business methods ------------------
	public Administrator findByPrincipal() {
		Administrator result;
		UserDetails userAccount;
		Authentication authentication;

		authentication = SecurityContextHolder.getContext().getAuthentication();
		userAccount = (UserDetails) authentication.getPrincipal();

		result = this.findByUsername(userAccount.getUsername());
		Assert.notNull(result, "El administrador no existe");

		return result;
	}

	// Private methods ------------------------
	private Administrator findByUsername(String username) {
		Administrator result;

		result = this.administratorRepository.findByUsername(username);

		return result;
	}

	public Collection<Administrator> findAll(){

		return this.administratorRepository.findAll();

	}

	private Administrator save(Administrator administrator) {
		return this.administratorRepository.saveAndFlush(administrator);
	}



}
