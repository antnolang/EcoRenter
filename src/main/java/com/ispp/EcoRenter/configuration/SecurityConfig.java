package com.ispp.EcoRenter.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;




@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	

	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/resources/**").permitAll()
				.antMatchers("/admin").hasAuthority("ADMIN")
				.antMatchers("/renter").hasAuthority("RENTER")
				.antMatchers("/owner").hasAuthority("OWNER")
				.anyRequest().authenticated()
				.and()
			.formLogin()
//			.loginPage("/login")
				.and()
			.logout()
				;
	}
	
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		
		auth.jdbcAuthentication()
		.dataSource(dataSource)	
		.usersByUsernameQuery("select username,password,is_banned "
				+ "from user_account "
				+ "where username = ?")
		.authoritiesByUsernameQuery("select u.username, a.authority "
				+ "from eco_renter.user_account u "
				+ "join eco_renter.user_account_authorities a "
				+ "on u.id = a.user_account_id "
				+ "where username = ?"); 
			
		
		
	}



	@Bean
	public PasswordEncoder passwordEncoder() {	    
		PasswordEncoder encoder =  NoOpPasswordEncoder.getInstance();
	    return encoder;
	}
	
}