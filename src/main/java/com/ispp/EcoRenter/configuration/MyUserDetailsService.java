package com.ispp.EcoRenter.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ispp.EcoRenter.repository.UserAccountRepository;
import com.ispp.EcoRenter.security.Authority;
import com.ispp.EcoRenter.security.UserAccount;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	UserAccountRepository userAccountRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<UserAccount> user = userAccountRepository.findByUsername(username);
		UserDetails result;
		
		if(user!=null) {
			Collection<Authority> auths = new ArrayList<Authority>();
			
			UserAccount currentUser = user.get();
			
			currentUser.setPassword(currentUser.getPassword());
			Authority newAuth = new Authority();
			
			
			String userAuth = userAccountRepository.findByUserId(currentUser.getId());
			
			newAuth.setAuthority(userAuth);
			
			auths.add(newAuth);
			
			currentUser.setAuthorities(auths);

		}
		
		result = user.map(MyUserDetails::new).get();
		
		return result;
	}
}
