package org.aston.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    public void sendUserCreatedMail(String email) {
        send(email,
                "Создание аккаунта",
                "Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
    }

    public void sendUserDeletedMail(String email) {
        send(email,
                "Удаление аккаунта",
                "Здравствуйте! Ваш аккаунт был удалён.");
    }

    private void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}


