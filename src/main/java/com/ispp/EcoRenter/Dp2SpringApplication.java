package com.ispp.EcoRenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.ispp.EcoRenter.repository.UserAccountRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserAccountRepository.class)
public class Dp2SpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(Dp2SpringApplication.class, args);
	}

}
