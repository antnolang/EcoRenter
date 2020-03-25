package com.ispp.EcoRenter.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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
    private Validator validator;
    
    @Autowired
    private ActorService actorService;
    
    @Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

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
    
    public Renter save(Renter renter) {
    	
    	
    	return this.renterRepository.saveAndFlush(renter);
    
    	
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
    
    public Renter register(RenterRegister renterRegister, BindingResult binding) {
    	Renter result = this.create();
    	UserAccount renterUserAccount = result.getUserAccount();
    	
    	//Comprobamos que las contraseñas coincidan, el usuario no exista y el iban sea correcto.
    	
    	Assert.isTrue(this.actorService.checkPassword(renterRegister.getPassword(), renterRegister.getPasswordMatch()), "Las contraseñas no coinciden.");
    	Assert.isTrue(this.actorService.checkNoRepeatedUsername(renterRegister.getUsername()),"El usuario elegido ya existe.");
    	Assert.isTrue(renterRegister.getIban().matches("[ES]{2}[0-9]{6}[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}"),"Iban incorrecto.");
    	
    	//Obtenemos valores del parametro renterRegister obtenido del formulario
    	
    	String name = renterRegister.getName();
    	String surname = renterRegister.getSurname();
    	String email = renterRegister.getEmail();
    	String telephone = renterRegister.getTelephoneNumber();
    	String image = renterRegister.getImage();
    	String username = renterRegister.getUsername();
    	String password = renterRegister.getPassword();
    	String iban = renterRegister.getIban();
    	
    	//Codificamos la password para persistirla asi en bd
    	String encodedPass = this.bCryptPasswordEncoder.encode(password);
    			
    	//Setteamos valores
    	
    	result.setName(name);
    	result.setSurname(surname);
    	result.setEmail(email);
    	result.setTelephoneNumber(telephone);
    	result.setImage(image);
    	result.setIban(iban);
    	
    	renterUserAccount.setUsername(username);
    	renterUserAccount.setPassword(encodedPass);
    	
    	
    	//Persistimos el resultado
    	
    	this.save(result);
    	
    	
    	return result;
    	
    	
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}