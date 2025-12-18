package org.aston.notification.kafka;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
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

import java.time.Instant;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"user-events"}
)
@ActiveProfiles("test")
public class NoitificationKafkaIT {
    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @RegisterExtension
    static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withPerMethodLifecycle(true);

    @Test
    void shouldSendEmailWhenUserCreated() throws Exception {
        kafkaTemplate.send("user-events",
                new UserEvent(UserEventType.CREATE, "test@mail.com", Instant.now()));

        await().atMost(5, SECONDS).untilAsserted(() -> {
            MimeMessage[] messages = greenMail.getReceivedMessages();
            assertThat(messages).hasSize(1);
            assertThat(messages[0].getSubject()).isEqualTo("Создание аккаунта");
        });
    }

    @Test
    void shouldSendEmailWhenUserDeleted() throws Exception {
        kafkaTemplate.send("user-events",
                new UserEvent(UserEventType.DELETE, "del@mail.com", Instant.now()));

        await().atMost(5, SECONDS).untilAsserted(() -> {
            MimeMessage message = greenMail.getReceivedMessages()[0];
            assertThat(message.getSubject()).isEqualTo("Удаление аккаунта");
        });
    }
}
