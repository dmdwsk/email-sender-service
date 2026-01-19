package com.codedmdwsk.emailsenderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "email_messages")
public class EmailMessage {
    @Id
    private String id;

    private List<String> to;
    private String subject;
    private String content;

    private EmailStatus status;

    private Integer attempt;
    private Instant lastAttemptAt;

    private String errorMessage;

    private Instant createdAt;
}
