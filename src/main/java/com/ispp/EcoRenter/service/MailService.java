package com.ispp.EcoRenter.service;

import java.util.List;

import javax.transaction.Transactional;

import com.ispp.EcoRenter.model.Actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

}