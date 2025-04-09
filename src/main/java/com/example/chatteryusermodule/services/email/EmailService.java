package com.example.chatteryusermodule.services.email;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendSimpleEmail(String toAddress, String subject, String messageText) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom("chatterystreams@gmail.com");
            mimeMessageHelper.setTo(toAddress);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(messageText);
            emailSender.send(message);
            log.info("Email sent to {}", toAddress);
        } catch (Exception e) {
            log.error("Failed to send email to {}", toAddress, e);
        }
    }
}
