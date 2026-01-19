package com.codedmdwsk.emailsenderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EmailSenderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailSenderServiceApplication.class, args);
    }
}
