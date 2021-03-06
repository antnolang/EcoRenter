package com.ispp.EcoRenter.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ispp.EcoRenter.model.Actor;

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

		email.setFrom(admin);
		email.setTo(receiver.getEmail());
		email.setCc(recipient.getEmail());
		email.setSubject(tipo);
		email.setText("El creador de esta disputa es: "+ recipient.getUserAccount().getUsername()+"\n"
				+ "El involucrado en esta disputa es: "+ receiver.getUserAccount().getUsername()+"\n"
				+"Siendo la id de la parcela en disputa la siguiente: " + smallholdingId +"\n"
				+"Siendo el texto descrito por el creador de la disputa el siguiente: "+"\n"
				+ descripcion);

		mailSender.send(email);

	}


   
  


}