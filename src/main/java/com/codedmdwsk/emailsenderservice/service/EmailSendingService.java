package com.codedmdwsk.emailsenderservice.service;

import com.codedmdwsk.emailsenderservice.repo.EmailMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendingService {
    private final JavaMailSender mailSender;
    private final EmailMessageRepository repo;

}
