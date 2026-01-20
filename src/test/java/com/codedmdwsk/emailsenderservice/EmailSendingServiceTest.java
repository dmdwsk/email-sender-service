package com.codedmdwsk.emailsenderservice;

import com.codedmdwsk.emailsenderservice.messaging.EmailRequestMessage;
import com.codedmdwsk.emailsenderservice.model.EmailMessage;
import com.codedmdwsk.emailsenderservice.model.EmailStatus;
import com.codedmdwsk.emailsenderservice.repo.EmailMessageRepository;
import com.codedmdwsk.emailsenderservice.service.EmailSendingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestMailConfig.class)
class EmailSendingServiceTest {

    @Autowired
    EmailSendingService service;

    @Autowired
    EmailMessageRepository repo;

    @Autowired
    JavaMailSender mailSender;

    @BeforeEach
    void clean() {
        repo.deleteAll();
        reset(mailSender);
    }

    @Test
    void createAndSend_shouldPersistAndSetSent_whenMailSenderSucceeds() {

        EmailRequestMessage req = new EmailRequestMessage();
        req.setTo(List.of("test@example.com"));
        req.setSubject("Subj");
        req.setContent("Body");


        EmailMessage saved = service.createAndSend(req);


        assertThat(saved.getId()).isNotBlank();
        assertThat(saved.getStatus()).isEqualTo(EmailStatus.SENT);
        assertThat(saved.getAttempt()).isGreaterThanOrEqualTo(1);
        assertThat(saved.getErrorMessage()).isNull();


        EmailMessage fromEs = repo.findById(saved.getId()).orElseThrow();
        assertThat(fromEs.getStatus()).isEqualTo(EmailStatus.SENT);


        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage mail = captor.getValue();
        assertThat(mail.getTo()).containsExactly("test@example.com");
        assertThat(mail.getSubject()).isEqualTo("Subj");
        assertThat(mail.getText()).isEqualTo("Body");
    }

    @Test
    void createAndSend_shouldPersistAndSetFailed_andSaveError_whenMailSenderThrows() {

        doThrow(new RuntimeException("SMTP down"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        EmailRequestMessage req = new EmailRequestMessage();
        req.setTo(List.of("test@example.com"));
        req.setSubject("Subj");
        req.setContent("Body");


        EmailMessage saved = service.createAndSend(req);


        assertThat(saved.getId()).isNotBlank();
        assertThat(saved.getStatus()).isEqualTo(EmailStatus.FAILED);
        assertThat(saved.getAttempt()).isGreaterThanOrEqualTo(1);
        assertThat(saved.getLastAttemptAt()).isNotNull();
        assertThat(saved.getErrorMessage()).contains("RuntimeException").contains("SMTP down");

        EmailMessage fromEs = repo.findById(saved.getId()).orElseThrow();
        assertThat(fromEs.getStatus()).isEqualTo(EmailStatus.FAILED);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
