package com.cradle.onlineshoppingpurchaseService.v1.services;

import com.cradle.onlineshoppingpurchaseService.v1.models.SendEmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;

@Service
@Slf4j
public class EmailService {

    @Autowired
   private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        Objects.requireNonNull(javaMailSender, "java mail sender is required");
    }








    @Async
    public void send(String from, String to,String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setText(text, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom(from);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e){
            log.info("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }




}
