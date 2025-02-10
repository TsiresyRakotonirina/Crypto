package com.example.crypto.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {

    private final JavaMailSender mailSender;


    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String toEmail, String token, int type, double montant) {
        String subject = (type == 1) ? "Confirmation de dépôt" : "Confirmation de retrait";
        String confirmationLink = "http://localhost:8080/confirm?token=" + token;

        String message = "Bonjour,\n\n" +
                "Veuillez confirmer votre " + ((type == 1) ? "dépôt" : "retrait") + " de " + montant + " Ar en cliquant sur ce lien :\n" +
                confirmationLink + "\n\nMerci.";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("admin@gmail.com");
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
