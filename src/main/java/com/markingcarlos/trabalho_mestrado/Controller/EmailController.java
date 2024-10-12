package com.markingcarlos.trabalho_mestrado.Controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

@Controller
public class EmailController {

    private final JavaMailSender mailSender;


    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String Email, String Titulo, String Descricao) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(Email);
        message.setSubject(Titulo);
        message.setText(Descricao);
        message.setFrom("trabalho_mestrado@gmail.com");
        mailSender.send(message);
    }
}
