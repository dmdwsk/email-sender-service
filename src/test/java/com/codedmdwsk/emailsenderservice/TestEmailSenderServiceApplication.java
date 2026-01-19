package com.codedmdwsk.emailsenderservice;

import org.springframework.boot.SpringApplication;

public class TestEmailSenderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(EmailSenderServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
