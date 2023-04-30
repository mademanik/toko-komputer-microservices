package org.tokkom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tokkom.model.Notification;
import org.tokkom.service.NotificationConsumer;

import java.util.List;

@RestController
@RequestMapping("/tokkom/api/notification")
public class NotificationController {

    @Autowired
    private NotificationConsumer notificationConsumer;

    public NotificationController(NotificationConsumer notificationConsumer) {
        this.notificationConsumer = notificationConsumer;
    }

    @GetMapping()
    public ResponseEntity<List<Notification>> getAllNotification() {
        List<Notification> notifications = notificationConsumer.getAll();
        return ResponseEntity.ok().body(notifications);
    }
}
