package com.ispp.EcoRenter.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import com.ispp.EcoRenter.model.Actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MailService {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        
        mailSender.setUsername("ecorenter@gmail.com");
        mailSender.setPassword("ecorenter2020.");
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        
        return mailSender;
    }

    protected void sendEmail(List<Actor> recipients, String subject, String content) {

        for(Actor a: recipients){
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(a.getEmail());
            email.setSubject(subject);
            email.setText(content);
        
            this.getJavaMailSender().send(email);
        }
        
    }

}