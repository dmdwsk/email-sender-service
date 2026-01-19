package com.codedmdwsk.emailsenderservice.messaging;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class EmailRequestMessage {
    @NotEmpty
    private List<String> to;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
