package com.ispp.EcoRenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Administrator;
import com.ispp.EcoRenter.repository.ActorRepository;
import com.ispp.EcoRenter.security.Authority;
import com.ispp.EcoRenter.security.UserAccount;

@Service
public class ActorService {

	private static final Log log = LogFactory.getLog(ActorService.class);
	
    // Repository ------------------------------------

    @Autowired
    private ActorRepository actorRepository;
    
    // Supporting services ----------------------------

    // Constructor ------------------------------------

    public ActorService(){
        super();
    }

    // CRUD Methods ------------------------------------
    public Actor findOne(int actorId) {
    	Actor principal, result;
    	Optional<Actor> optionalActor;
    	boolean isARenter, isRoleActor, isAOwner, isMyProfile;
    	
    	principal = this.findByPrincipal();
    	
    	Assert.isTrue(!principal.getUserAccount().getIsBanned(), "Usuario baneado");
    	
    	optionalActor = this.actorRepository.findById(actorId);
    	
    	result = optionalActor.orElse(principal);
    	
    	isMyProfile = principal.getId() == result.getId();
    	
    	isAOwner = this.isASpecificRole(principal, Authority.OWNER);
    	isARenter = this.isASpecificRole(principal, Authority.RENTER);
    	
    	if (isAOwner && !isMyProfile) {
    		isRoleActor = this.isASpecificRole(result, Authority.RENTER);
    		
    		Assert.isTrue(isRoleActor, "Un arrendatario no puede acceder a info de otro arrendatario");
    	} else if (isARenter && !isMyProfile) {
    		isRoleActor = this.isASpecificRole(result, Authority.OWNER);
  
    		Assert.isTrue(isRoleActor, "Un propietario no puede acceder a info de otro propietario");
    	}
    	
    	return result;
    }
    
    // Other business methods
    public void ban(Actor actor) {
    	Assert.notNull(actor, "Actor desconocido");
    	Assert.isTrue(!actor.getUserAccount().getIsBanned(), "El actor ya esta baneado");
    	Assert.isTrue(this.findByPrincipal() instanceof Administrator, "Accion realizable por un admin");
    	
    	UserAccount userAccount;
    	
    	userAccount = actor.getUserAccount();
    	
    	userAccount.setIsBanned(true);
    	this.actorRepository.flush();
    	
    	Assert.isTrue(actor.getUserAccount().getIsBanned(), "Banear actor");
    }
    
    public void unBan(Actor actor) {
    	Assert.notNull(actor, "Actor desconocido");
    	Assert.isTrue(actor.getUserAccount().getIsBanned(), "El actor ya esta baneado");
    	Assert.isTrue(this.findByPrincipal() instanceof Administrator, "Accion realizable por un admin");
    	
    	UserAccount userAccount;
    	
    	userAccount = actor.getUserAccount();
    	
    	userAccount.setIsBanned(false);
    	this.actorRepository.flush();
    	
    	Assert.isTrue(!actor.getUserAccount().getIsBanned(), "Desbanear actor");
    }
    
    public Actor findByPrincipal(){
		Actor result;
    	UserDetails userAccount;
        Authentication authentication;

        authentication = SecurityContextHolder.getContext().getAuthentication();
        userAccount = (UserDetails) authentication.getPrincipal();
        
        result = this.findByUsername(userAccount.getUsername());
		Assert.notNull(result,"El actor no existe");
		
		return result;
    }
    
    public boolean isASpecificRole(Actor actor, String role) {
    	boolean result;
    	Authority authority;
    	
    	authority = new Authority();
    	authority.setAuthority(role);
    	  	
    	result = actor.getUserAccount().getAuthorities().contains(authority);
    	
    	return result;
    }
    
    public String getEncodedIban(String iban) {
    	String result;
    	
    	result = (iban != null && iban != "" && !iban.isEmpty())
    			? iban.substring(0, 4) + " **** **** **** **** " + iban.subSequence(19, 23)
    			: "";
    	
    	return result;
    }
    
    public String getRole(Actor actor) {
    	String result;
    	List<Authority> authorities;
    	
    	authorities = new ArrayList<Authority>(actor.getUserAccount().getAuthorities());
    	result = "";
    	for (Authority a: authorities) {
    		result += a.getAuthority() + " ";
    	}
    	 	
    	return result;
    }
    
    private Actor findByUsername(String username) {
    	Actor result;
    	
    	result = this.actorRepository.findByUsername(username);
    	
    	return result;
    }
    
    public boolean checkPassword(String password, String passwordMatch) {
    	boolean result = false;
    	
    	if(password.equals(passwordMatch)) {
    		result = true;
    	}
    	
    	return result;
    }
    
    public boolean checkNoRepeatedUsername(String username) {
    	boolean result = false;
    	
    	
    	if(this.actorRepository.findByUsername(username) == null) {
    		result = true;
    	}
    	
    	
    	return result;
    }
    
}