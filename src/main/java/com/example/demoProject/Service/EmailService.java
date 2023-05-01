package com.example.demoProject.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    @Autowired
    JavaMailSender mailSender;

    public void sendOtpMessage(String to, String subject, String message) throws MessagingException {

        MimeMessage msg = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setFrom("Mervron_Boss@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);
        mailSender.send(msg);
    }
    public void sendPasswordResetLink(String to, String subject, String message) throws MessagingException {

        MimeMessage msg = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setFrom("Mervron@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);
        helper.addInline("clickLink", new ClassPathResource("img/avatar.jpg"));
        mailSender.send(msg);
    }
}
