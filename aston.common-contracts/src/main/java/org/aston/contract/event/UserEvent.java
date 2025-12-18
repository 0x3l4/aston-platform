package org.aston.contract.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
    private UserEventType eventType;
    private String email;
    private Instant timestamp;
}