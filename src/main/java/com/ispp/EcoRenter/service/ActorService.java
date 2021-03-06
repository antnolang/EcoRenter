package com.ispp.EcoRenter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Administrator;
import com.ispp.EcoRenter.repository.ActorRepository;
import com.ispp.EcoRenter.security.Authority;
import com.ispp.EcoRenter.security.UserAccount;

@Service
@Transactional
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
    	
    	Assert.isTrue(!principal.getUserAccount().getIsBanned(),
    				  "Usuario baneado");
    	
    	optionalActor = this.actorRepository.findById(actorId);
    	
    	result = optionalActor.orElse(principal);
    	
    	isMyProfile = principal.getId() == result.getId();
    	
    	isAOwner = this.isASpecificRole(principal, Authority.OWNER);
    	isARenter = this.isASpecificRole(principal, Authority.RENTER);
    	
    	if (isAOwner && !isMyProfile) {
    		isRoleActor = this.isASpecificRole(result, Authority.RENTER);
    		
    		Assert.isTrue(isRoleActor,
    				      "Un arrendatario no puede acceder a info de otro arrendatario");
    	} else if (isARenter && !isMyProfile) {
    		isRoleActor = this.isASpecificRole(result, Authority.OWNER);
  
    		Assert.isTrue(isRoleActor,
    				      "Un propietario no puede acceder a info de otro propietario");
    	}
    	
    	return result;
    }
    
    // Other business methods
    public void ban(Actor actor) {
    	Assert.notNull(actor, "Actor desconocido");
    	Assert.isTrue(!actor.getUserAccount().getIsBanned(),
    				  "El actor ya esta baneado");
    	Assert.isTrue(this.findByPrincipal() instanceof Administrator,
    			      "Accion realizable por un admin");
    	
    	UserAccount userAccount;
    	
    	userAccount = actor.getUserAccount();
    	
    	userAccount.setIsBanned(true);
    	this.actorRepository.flush();
    	
    	Assert.isTrue(actor.getUserAccount().getIsBanned(), "Banear actor");
    }
    
    public void unBan(Actor actor) {
    	Assert.notNull(actor, "Actor desconocido");
    	Assert.isTrue(actor.getUserAccount().getIsBanned(), "El actor ya esta baneado");
    	Assert.isTrue(this.findByPrincipal() instanceof Administrator,
    				  "Accion realizable por un admin");
    	
    	UserAccount userAccount;
    	
    	userAccount = actor.getUserAccount();
    	
    	userAccount.setIsBanned(false);
    	this.actorRepository.flush();
    	
    	Assert.isTrue(!actor.getUserAccount().getIsBanned(), "Desbanear actor");
    }
    
    public Actor findByPrincipal(){
		Actor result;
    	UserDetails userAccount;

        userAccount = this.findUserDetailsByPrincipal();
        
        result = this.findByUsername(userAccount.getUsername());
		Assert.notNull(result,"El actor no existe");
		
		return result;
    }
    
    public UserDetails findUserDetailsByPrincipal() {
    	UserDetails userAccount;
        Authentication authentication;

        authentication = SecurityContextHolder.getContext().getAuthentication();
        userAccount = (UserDetails) authentication.getPrincipal();
        
        return userAccount;
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
    	
    	result = (StringUtils.hasText(iban))
    			? iban.substring(0, 4) + " **** **** **** **** " + iban.subSequence(20, 24)
    			: "";
    	
    	return result;
    }
    
    public String getRole(Actor actor) {
    	String result;
    	List<Authority> authorities;
    	String authority;
    	
    	authorities = new ArrayList<Authority>(actor.getUserAccount().getAuthorities());
    	result = "";
    	for (Authority a: authorities) {
    		authority = a.getAuthority();
    		
    		result += authority  + " ";
    	}
    	 	
    	return result.trim();
    }
    
    public String getDisplayRole(Actor actor) {
    	String result;
    	
    	result = this.getRole(actor);
    	
    	if (result.equals("RENTER")) {
    		result = "ARRENDATARIO";
    	} else if (result.equals("OWNER")) {
    		result = "PROPIETARIO";
    	} else {
    		result = "ADMINISTRADOR";
    	}
    	
    	return result.trim();
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
    
    public Collection<Actor> findAllExceptAdmin(){
    	Collection<Actor> actors = this.actorRepository.findAll();
    	Collection<Actor> result = new ArrayList<Actor>();
    	for(Actor a : actors) {
    		if(!(a instanceof Administrator)) {
    			result.add(a);
    		}
    	}
    	
    	
    	return result;
    }
    
    public Page<Actor> findPaginated(Pageable pageable, Collection<Actor> actors) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Actor> list;
        ArrayList<Actor> smaList= new ArrayList<>(actors);
 
        if (smaList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, smaList.size());
            list = smaList.subList(startItem, toIndex);
        }
 
        Page<Actor> smPage
          = new PageImpl<Actor>(list, PageRequest.of(currentPage, pageSize), smaList.size());
 
        return smPage;
    }
    
    
}
