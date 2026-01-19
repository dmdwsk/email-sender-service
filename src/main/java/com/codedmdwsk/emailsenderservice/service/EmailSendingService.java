package com.codedmdwsk.emailsenderservice.service;

import com.codedmdwsk.emailsenderservice.messaging.EmailRequestMessage;
import com.codedmdwsk.emailsenderservice.model.EmailMessage;
import com.codedmdwsk.emailsenderservice.model.EmailStatus;
import com.codedmdwsk.emailsenderservice.repo.EmailMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EmailSendingService {
    private final JavaMailSender mailSender;
    private final EmailMessageRepository repo;

    public EmailMessage createAndSend(EmailRequestMessage req) {
        EmailMessage entity = EmailMessage.builder()
                .to(req.getTo())
                .subject(req.getSubject())
                .content(req.getContent())
                .status(EmailStatus.NEW)
                .attempt(0)
                .createdAt(Instant.now())
                .build();

        entity = repo.save(entity);
        return trySend(entity);
    }

    public EmailMessage trySend(EmailMessage entity) {
        entity.setAttempt(entity.getAttempt() == null ? 1 : entity.getAttempt() + 1);
        entity.setLastAttemptAt(Instant.now());

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(entity.getTo().toArray(String[]::new));
            mail.setSubject(entity.getSubject());
            mail.setText(entity.getContent());

            mailSender.send(mail);

            entity.setStatus(EmailStatus.SENT);
            entity.setErrorMessage(null);
        } catch (Exception ex) {
            entity.setStatus(EmailStatus.FAILED);
            entity.setErrorMessage(ex.getClass().getName() + ": " + ex.getMessage());
        }

        return repo.save(entity);
    }

}
