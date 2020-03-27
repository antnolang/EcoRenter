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
import com.ispp.EcoRenter.model.Administrator;
import com.ispp.EcoRenter.model.Comment;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Renter;
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

				Collection<Comment> commentsOfThisRent = this.commentService.findCommentsByRentOut(r.getId());

				if(!commentsOfThisRent.isEmpty()) {
					for(Comment c : commentsOfThisRent) {
						this.commentService.delete(c);


					}

					this.rentoutService.delete(r);
				}else {
					this.rentoutService.delete(r);
				}


			}
			this.renterService.delete(renter);
		}else {
			this.renterService.delete(renter);
		}



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

	private Administrator save(Administrator administrator) {
		return this.administratorRepository.saveAndFlush(administrator);
	}

}
