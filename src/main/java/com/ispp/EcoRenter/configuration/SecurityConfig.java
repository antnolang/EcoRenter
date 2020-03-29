package com.ispp.EcoRenter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/static/**").permitAll()
		.antMatchers("/smallholding/**").permitAll()
		.antMatchers("/actor/administrator/**").hasAnyAuthority("ADMIN")
		.antMatchers("/photo/authenticated/**").authenticated()
		.antMatchers("/actor/authenticated/**").authenticated()
		.antMatchers("/owner/smallholding/**").hasAnyAuthority("OWNER")
		.antMatchers("/renter/smallholding/**").hasAnyAuthority("RENTER")
		.antMatchers("/administrator/customisation/**").hasAnyAuthority("ADMIN")
		.antMatchers("/resources/**").permitAll()
		.antMatchers("/actor/renter/register").anonymous()
		.antMatchers("/actor/renter/owner").anonymous()
		.and()
		.formLogin()
		.loginPage("/login")
		.defaultSuccessUrl("/home") 
		//.failureUrl("/login-error"), ahora mismo cuando peta se controla en la vista /login, pero se puede hacer una vista nueva, como veamos.
		.and()
		.logout()
		.logoutSuccessUrl("/home")
		;
	}

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(5);
	}


	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(this.passwordEncoder());
		
		return authenticationProvider;
	}
}




