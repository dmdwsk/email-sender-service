# 📧 Email Sender Service
## 🔍 Overview

Email Sender Service is a Spring Boot–based microservice responsible for centralized, asynchronous, and reliable email delivery.

The service receives email requests via a message broker, persists them in ElasticSearch, sends emails using SMTP, and tracks their delivery status.

It is designed to be fault-tolerant:
if the SMTP server is temporarily unavailable, email messages are automatically retried in the background.

### ✨ Key Features

📨 Asynchronous email delivery via RabbitMQ

🧠 Centralized email logic (no duplication across microservices)

🗄️ Persistent email status tracking in ElasticSearch

🔁 Automatic retry mechanism for failed messages

✉️ SMTP integration using Spring’s JavaMailSender

🐳 Fully containerized with Docker & Docker Compose

🧪 Integration tests covering both success and failure scenarios

Technology Stack

Java 21

Spring Boot

RabbitMQ

ElasticSearch + Kibana

SMTP (MailHog for local development)

Docker & Docker Compose

Running the Project Locally
Prerequisites

Docker

Docker Compose

No local JDK installation is required.

1. Create .env file

Create a .env file in the project root:

```
SMTP_HOST=mailhog
SMTP_PORT=1025
SMTP_USER=
SMTP_PASS=
```
2. Build Docker image

```
docker build -t email-sender-service:local .
```

3. Start all services

```
docker-compose up
```
Exposed Services:

Email Sender Service - http://localhost:8080

RabbitMQ Management UI - http://localhost:15672

ElasticSearch - http://localhost:9200

Kibana - http://localhost:5601

MailHog (SMTP UI) - http://localhost:8025

Sending an Email via RabbitMQ

Open RabbitMQ Management UI

Navigate to Exchanges

Open exchange email.exchange

Click Publish message

Fill in:

Routing key: email.send

Payload:
```
{
  "to": ["test@example.com"],
  "subject": "Hello",
  "content": "From RabbitMQ"
}
```

Verifying Results
Email Delivery

Open MailHog UI:

http://localhost:8025

The sent email should appear in the inbox.

Email Status in ElasticSearch

curl http://localhost:9200/email_messages/_search?pretty

Retry Mechanism

If email delivery fails:

the message is saved with status FAILED

error details are stored in errorMessage

a scheduled background job retries sending failed emails every 5 minutes

on success, status is updated to SENT

Running Tests

```
mvn test
```

Summary

This service provides a reliable, scalable, and testable solution for asynchronous email delivery in a microservice architecture, fulfilling all functional and non-functional requirements of the assignment.