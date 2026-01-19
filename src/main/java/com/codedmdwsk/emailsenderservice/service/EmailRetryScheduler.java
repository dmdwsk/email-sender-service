package com.codedmdwsk.emailsenderservice.service;

import com.codedmdwsk.emailsenderservice.model.EmailStatus;
import com.codedmdwsk.emailsenderservice.repo.EmailMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailRetryScheduler {

    private final EmailMessageRepository repo;
    private final EmailSendingService emailSendingService;

    @Scheduled(fixedDelayString = "PT5M")
    public void retryFailed() {
        var failed = repo.findTop50ByStatusOrderByCreatedAtAsc(EmailStatus.FAILED);
        for (var msg : failed) {
            emailSendingService.trySend(msg);
        }
    }
}
