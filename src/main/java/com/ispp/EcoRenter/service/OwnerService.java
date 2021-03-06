package com.ispp.EcoRenter.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.ispp.EcoRenter.configuration.MyUserDetailsService;
import com.ispp.EcoRenter.form.OwnerForm;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.register.OwnerRegister;
import com.ispp.EcoRenter.repository.OwnerRepository;
import com.ispp.EcoRenter.security.Authority;
import com.ispp.EcoRenter.security.UserAccount;

@Service
@Transactional
public class OwnerService {

	// Repository

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	// Supporting services

	// Constructor

	public OwnerService(){
		super();
	}

	// CRUD Methods


	public Owner create() {
		final Owner result = new Owner();
		final UserAccount userAccount = this.userAccountService.create();

		final Authority auth = new Authority();
		auth.setAuthority(Authority.OWNER);
		userAccount.addAuthority(auth);
		userAccount.setIsBanned(false);


		result.setUserAccount(userAccount);

		return result;

	}

	
	public Owner save(Owner owner) {


		return this.ownerRepository.saveAndFlush(owner);


	}
	
	public void delete(Owner owner) {
		
		this.ownerRepository.delete(owner);
	}


	public Owner findOne(int ownerId) {
		Assert.isTrue(ownerId > 0, "Invalid ownerId");

		Optional<Owner> owner;
		Owner result;

		owner = this.ownerRepository.findById(ownerId);

		result = owner.orElse(null);

		return result;
	}

	// Other business methods

	public Owner findByPrincipal(){
		Owner result;
		UserDetails userAccount;
		Authentication authentication;

		authentication = SecurityContextHolder.getContext().getAuthentication();
		userAccount = (UserDetails) authentication.getPrincipal();

		result = this.ownerRepository.findOwnerByUsername(userAccount.getUsername());
		Assert.notNull(result,"El propietario no existe");

		return result;
	}


	public Owner edit(OwnerForm ownerForm) {
		String name, surname, email, telephoneNumber;
		String username, password, passwordMatch, iban;
		String encodedPassword, usernameDB;
		UsernamePasswordAuthenticationToken usernameToken;
		MultipartFile file;
		Photo photo;
		Owner result;
		UserAccount userAccount;
		UserDetails userDetails;
		
		result = this.findByPrincipal();
		
		name = ownerForm.getName();
		surname = ownerForm.getSurname();
		email = ownerForm.getEmail();
		telephoneNumber = ownerForm.getTelephoneNumber();
		username = ownerForm.getUsername();
		password = ownerForm.getPassword();
		passwordMatch = ownerForm.getPasswordMatch();
		iban = ownerForm.getIban();
		file = ownerForm.getFile();
		
		userAccount = result.getUserAccount();
		// Las contraseñas deben coincidir.
		Assert.isTrue(this.actorService.checkPassword(password, passwordMatch),
				      "Las contraseñas no coinciden.");
				
		usernameDB = result.getUserAccount().getUsername();
		// Si el usuario ha decidido cambiar de username, comprobar que no existe
		if (!usernameDB.equals(username)) {
			Assert.isTrue(this.actorService.checkNoRepeatedUsername(username),
					      "El usuario elegido ya existe.");
		}
		
		// Si el usuario ha introducido un nuevo iban, comprobamos que sea válido
		// Si no ha introducido ningun valor, para el iban se mantiene el que tenía anteriormente
		if (StringUtils.hasText(iban)) {
			Assert.isTrue(iban.matches("[ES]{2}[0-9]{6}[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}"),
					      "Iban incorrecto.");
		
			result.setIban(iban.trim());
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
		
		usernameToken = new UsernamePasswordAuthenticationToken(userDetails,
																null,
																userAccount.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(usernameToken);
		
		return result;
	}
	
	public Owner register(OwnerRegister ownerRegister, BindingResult binding) {
		Owner result = this.create();
		UserAccount renterUserAccount = result.getUserAccount();
		Photo photo;

		//Comprobamos que las contraseñas coincidan, el usuario no exista y el iban sea correcto.

		Assert.isTrue(this.actorService.checkPassword(ownerRegister.getPassword(),
													  ownerRegister.getPasswordMatch()),
				                                      "Las contraseñas no coinciden.");
		Assert.isTrue(this.actorService.checkNoRepeatedUsername(ownerRegister.getUsername()),
				                                                "El usuario elegido ya existe.");
		

		//Obtenemos valores del parametro renterRegister obtenido del formulario

		String name = ownerRegister.getName();
		String surname = ownerRegister.getSurname();
		String email = ownerRegister.getEmail();
		String telephone = ownerRegister.getTelephoneNumber();
		String username = ownerRegister.getUsername();
		String password = ownerRegister.getPassword();
		
		int months = 0;
		MultipartFile file = ownerRegister.getFile();

		//Codificamos la password para persistirla asi en bd
		String encodedPass = this.passwordEncoder.encode(password);

		//Setteamos valores

		result.setName(name);
		result.setSurname(surname);
		result.setEmail(email);
		result.setTelephoneNumber(telephone);
		result.setAccumulatedMonths(months);
		
		if (file != null) {
			photo = this.photoService.storeImage(file);
			
			if (photo != null) {
				result.setPhoto(photo);
			}
			
		}

		renterUserAccount.setUsername(username);
		renterUserAccount.setPassword(encodedPass);

		//Persistimos el resultado
		this.save(result);

		return result;
	}
}