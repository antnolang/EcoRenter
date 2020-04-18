package com.ispp.EcoRenter.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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


	public void sendEmailDispute(Actor recipient, Actor receiver,String admin, String tipo, String descripcion,int smallholdingId) {

		SimpleMailMessage email = new SimpleMailMessage();

		email.setFrom(recipient.getEmail());
		email.setTo(admin);
		email.setCc(receiver.getEmail());
		email.setSubject(tipo);
		email.setText("El creador de esta disputa es: "+ recipient.getUserAccount().getUsername()+"\n"
				+ "El involucrado en esta disputa es: "+ receiver.getUserAccount().getUsername()+"\n"
				+"Siendo la id de la parcela en disputa la siguiente: " + smallholdingId +"\n"
				+"Siendo el texto descrito por el creador de la disputa el siguiente: "+"\n"
				+ descripcion);

		mailSender.send(email);

	}


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