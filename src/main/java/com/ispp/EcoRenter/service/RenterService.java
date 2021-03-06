package com.ispp.EcoRenter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.ispp.EcoRenter.configuration.MyUserDetailsService;
import com.ispp.EcoRenter.form.RenterForm;
import com.ispp.EcoRenter.model.CreditCard;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.register.RenterRegister;
import com.ispp.EcoRenter.repository.RenterRepository;
import com.ispp.EcoRenter.security.Authority;
import com.ispp.EcoRenter.security.UserAccount;

@Service
@Transactional
public class RenterService {

    // Repository

    @Autowired
    private RenterRepository renterRepository;
    
    @Autowired
    private UserAccountService userAccountService;
    
    @Autowired
    private PhotoService photoService;
    
    @Autowired
    private ActorService actorService;
    
    @Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
	private MyUserDetailsService myUserDetailsService;
    
    // Constructor

    public RenterService(){
        super();
    }

    // CRUD methods
    
    public Renter create() {
    	final Renter result = new Renter();
    	final UserAccount userAccount = this.userAccountService.create();
    	
    	final Authority auth = new Authority();
    	auth.setAuthority(Authority.RENTER);
    	userAccount.addAuthority(auth);
    	userAccount.setIsBanned(false);
    	
    	result.setUserAccount(userAccount);
    	result.setCreditCards(Collections.emptySet());
    	
    	return result;	
    }
    
    
    public Renter findOne(int renterId) {
    	Assert.isTrue(renterId > 0, "Invalid renterId");
    	
    	Optional<Renter> renter;
    	Renter result;
    	
    	renter = this.renterRepository.findById(renterId);
    	
    	result = renter.orElse(null);
    	
    	return result;
    }
    
    public List<Renter> findAll(){
    	
    	return this.renterRepository.findAll();
    	
    }
    
    public Renter save(Renter renter) {
   
    	return this.renterRepository.saveAndFlush(renter);
    
    }
    
    
    public void delete(Renter renter) {
    	this.renterRepository.delete(renter);
    }
    // Other business methods

    public Renter findByPrincipal(){
        Renter result;
        UserDetails userAccount;
        Authentication authentication;

        authentication = SecurityContextHolder.getContext().getAuthentication();
        userAccount = (UserDetails) authentication.getPrincipal();
        
        result = this.renterRepository.findRenterByUsername(userAccount.getUsername());
        Assert.notNull(result,"El arrendatario no existe");

        return result;
    }
    
    public Renter edit(RenterForm renterForm) {
    	String name, surname, email, telephoneNumber, username;
    	String password, passwordMatch, encodedPassword, usernameDB;
    	UsernamePasswordAuthenticationToken usernameToken;
    	Renter result;
    	UserAccount userAccount;
    	MultipartFile file;
    	Photo photo;
    	UserDetails userDetails;
    	
    	result = this.findByPrincipal();
    	
    	name = renterForm.getName();
		surname = renterForm.getSurname();
		email = renterForm.getEmail();
		telephoneNumber = renterForm.getTelephoneNumber();
		username = renterForm.getUsername();
		password = renterForm.getPassword();
		passwordMatch = renterForm.getPasswordMatch();
		file = renterForm.getFile();
		
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
    
    public Renter register(RenterRegister renterRegister, BindingResult binding) {
    	Renter result = this.create();
    	UserAccount renterUserAccount = result.getUserAccount();
    	Photo photo;
    	
    	//Comprobamos que las contraseñas coincidan, el usuario no exista y el iban sea correcto.
    	
    	Assert.isTrue(this.actorService.checkPassword(renterRegister.getPassword(),
    												  renterRegister.getPasswordMatch()),
    				  "Las contraseñas no coinciden.");
    	Assert.isTrue(this.actorService.checkNoRepeatedUsername(renterRegister.getUsername()),
    			      "El usuario elegido ya existe.");
    	
    	
    
    	//Obtenemos valores del parametro renterRegister obtenido del formulario
    	
    	String name = renterRegister.getName();
    	String surname = renterRegister.getSurname();
    	String email = renterRegister.getEmail();
    	String telephone = renterRegister.getTelephoneNumber();
    	String username = renterRegister.getUsername();
    	String password = renterRegister.getPassword();
    	MultipartFile file = renterRegister.getFile();
    	
    	//Codificamos la password para persistirla asi en bd
    	String encodedPass = this.passwordEncoder.encode(password);
    			
    	//Setteamos valores
    	
    	result.setName(name);
    	result.setSurname(surname);
    	result.setEmail(email);
    	result.setTelephoneNumber(telephone);
    	
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
    
    public Renter findRenterByCreditCard(int creditCardId) {
    	Renter result;
    	
    	result = this.renterRepository.findRenterByCreditCard(creditCardId);
    	
    	return result;	
    }
    	
    public List<Renter> renterExport(List<Renter> renters) {
    	List<Renter> result = new ArrayList<Renter>();
    	
    	for(Renter r : renters) {
    		//TODO: Exportar renters
    	}
    	    	
    	return result;
    }
    
    protected void addCreditCard(Renter renter, CreditCard creditCard) {
    	Collection<CreditCard> creditCards;
    	
    	creditCards = renter.getCreditCards();
    	creditCards.add(creditCard);
    }
    
    protected void removeCreditCard(Renter renter, CreditCard creditCard) {
    	Collection<CreditCard> creditCards;
    	
    	creditCards = renter.getCreditCards();
    	creditCards.remove(creditCard);
    }
    
}