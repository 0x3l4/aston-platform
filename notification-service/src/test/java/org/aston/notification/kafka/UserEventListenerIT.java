package org.aston.notification.kafka;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.aston.contract.event.UserEvent;
import org.aston.contract.event.UserEventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
        topics = "user-events",
        partitions = 1
)
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "app.kafka.user-topic=user-events"
})
class UserEventListenerIT {

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(
            new ServerSetup(3025, null, ServerSetup.PROTOCOL_SMTP)
    );

    @Test
    void shouldSendMailOnKafkaEvent() throws Exception {
        UserEvent event = new UserEvent(
                UserEventType.CREATE,
                "test@example.com",
                Instant.now()
        );

        kafkaTemplate.send("user-events", event);

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    MimeMessage[] messages = greenMail.getReceivedMessages();
                    assertThat(messages).hasSize(1);
                    assertThat(messages[0].getSubject())
                            .isEqualTo("Создание аккаунта");
                });
    }
}

