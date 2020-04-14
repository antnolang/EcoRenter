package com.ispp.EcoRenter.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Administrator;

@Service
@Transactional
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    protected void sendEmail(List<Actor> recipients, String subject, String content) {

        for(Actor a: recipients){
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(a.getEmail());
            email.setSubject(subject);
            email.setText(content);
            
            mailSender.send(email);
        }
        
    }
    
    
    public void sendEmailDispute(Actor recipient, Actor receiver,Administrator admin, String tipo, String descripcion) {
    	
    	SimpleMailMessage email = new SimpleMailMessage();
    	
    	email.setFrom(recipient.getEmail());
    	email.setTo(admin.getEmail());
    	email.setCc(receiver.getEmail());
    	email.setSubject(tipo);
    	email.setText(descripcion);
    	
    	mailSender.send(email);
    	
    }
    

}