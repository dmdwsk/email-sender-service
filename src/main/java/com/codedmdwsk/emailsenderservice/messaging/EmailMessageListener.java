package com.codedmdwsk.emailsenderservice.messaging;

import com.codedmdwsk.emailsenderservice.config.RabbitMqConfig;
import com.codedmdwsk.emailsenderservice.service.EmailSendingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailMessageListener {
    private final EmailSendingService emailSendingService;
    @RabbitListener(queues = RabbitMqConfig.QUEUE)
    public void onMessage(@Valid EmailRequestMessage msg) {
        emailSendingService.createAndSend(msg);
    }
}
