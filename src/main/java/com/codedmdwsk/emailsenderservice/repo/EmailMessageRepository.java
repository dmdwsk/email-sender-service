package com.codedmdwsk.emailsenderservice.repo;

import com.codedmdwsk.emailsenderservice.model.EmailMessage;
import com.codedmdwsk.emailsenderservice.model.EmailStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EmailMessageRepository extends ElasticsearchRepository<EmailMessage, String> {
    List<EmailMessage> findTop50ByStatusOrderByCreatedAtAsc(EmailStatus status);
}
