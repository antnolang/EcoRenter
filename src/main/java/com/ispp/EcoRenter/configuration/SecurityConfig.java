package com.ispp.EcoRenter.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ispp.EcoRenter.security.Authority;



@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	DataSource dataSource;

	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/static/**").permitAll()
		.antMatchers("/smallholding/**").permitAll()
		.antMatchers("/actor/administrator/**").hasAnyAuthority(Authority.ADMIN)
		.antMatchers("/actor/display").authenticated()
		.antMatchers("/owner/smallholding/**").hasAnyAuthority("OWNER")
		.antMatchers("/resources/**").permitAll()
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
	public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}


	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
}




