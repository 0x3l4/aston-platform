package org.aston.notification.kafka;

import lombok.RequiredArgsConstructor;
import org.aston.contract.event.UserEvent;
import org.aston.notification.MailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventListener {
    private final MailService mailService;

    @KafkaListener(topics = "${app.kafka.user-topic}")
    public void handle(UserEvent event) {
        switch (event.getEventType()) {
            case CREATE -> mailService.sendUserCreatedMail(event.getEmail());
            case DELETE -> mailService.sendUserDeletedMail(event.getEmail());
        }
    }
}
