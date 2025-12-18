package org.aston.kafka;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.aston.contract.event.UserEvent;
import org.aston.contract.event.UserEventType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Value("${app.kafka.user-topic:user-events}")
    private String topic;

    public void sendUserCreated(String email) {
        kafkaTemplate.send(topic,
                new UserEvent(UserEventType.CREATE, email, Instant.now()));
    }

    public void sendUserDeleted(String email) {
        kafkaTemplate.send(topic,
                new UserEvent(UserEventType.DELETE, email, Instant.now()));
    }
}