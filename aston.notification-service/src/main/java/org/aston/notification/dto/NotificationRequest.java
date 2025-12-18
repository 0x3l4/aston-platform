package org.aston.notification.dto;

import lombok.Data;
import org.aston.contract.event.UserEventType;

@Data
public class NotificationRequest {
    private String email;
    private UserEventType eventType;
}
