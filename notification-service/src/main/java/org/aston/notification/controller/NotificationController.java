package org.aston.notification.controller;

import lombok.RequiredArgsConstructor;
import org.aston.contract.event.UserEventType;
import org.aston.notification.dto.NotificationRequest;
import org.aston.notification.MailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final MailService mailService;

    @PostMapping
    public void send(@RequestBody NotificationRequest request) {
        if (request.getEventType() == UserEventType.CREATE) {
            mailService.sendUserCreatedMail(request.getEmail());
        } else {
            mailService.sendUserDeletedMail(request.getEmail());
        }
    }
}
