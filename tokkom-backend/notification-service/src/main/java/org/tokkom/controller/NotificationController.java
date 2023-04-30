package org.tokkom.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tokkom.dto.NotificationSetExpiredRequest;
import org.tokkom.model.Notification;
import org.tokkom.service.NotificationConsumer;

import java.util.List;

@RestController
@RequestMapping("/tokkom/api/notification")
@Slf4j
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

    @PostMapping("/setExpired")
    public ResponseEntity<String> setExpiredNotification(@RequestBody NotificationSetExpiredRequest notificationSetExpiredRequest) {
        Boolean setToExpire = notificationConsumer.setExpiration(notificationSetExpiredRequest);

        if (!setToExpire) {
            log.error("Failed set expiration with id : " + notificationSetExpiredRequest.getId());
            return ResponseEntity.internalServerError().body("Failed set expiration with id : " + notificationSetExpiredRequest.getId());
        }

        return ResponseEntity.ok().body("Notification expiration with id : " + notificationSetExpiredRequest.getId() + " successfully updated");
    }
}
