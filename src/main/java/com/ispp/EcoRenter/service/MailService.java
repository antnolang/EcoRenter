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

    protected void sendEmail(final List<Actor> recipients, final String subject, final String content) {

	for (Actor a : recipients) {
	    SimpleMailMessage email = new SimpleMailMessage();
	    email.setTo(a.getEmail());
	    email.setSubject(subject);
	    email.setText(content);

	    this.mailSender.send(email);
	}

    }

    public void sendEmailDispute(final Actor recipient, final Actor receiver, final String admin, final String tipo,
	    final String descripcion, final int smallholdingId) {

	SimpleMailMessage email = new SimpleMailMessage();

	email.setFrom(recipient.getEmail());
	email.setTo(admin);
	email.setCc(receiver.getEmail());
	email.setSubject(tipo);
	email.setText("El creador de esta disputa es: " + recipient.getUserAccount().getUsername() + "\n"
		+ "El involucrado en esta disputa es: " + receiver.getUserAccount().getUsername() + "\n"
		+ "Siendo la id de la parcela en disputa la siguiente: " + smallholdingId + "\n"
		+ "Siendo el texto descrito por el creador de la disputa el siguiente: " + "\n" + descripcion);

	this.mailSender.send(email);

    }

}