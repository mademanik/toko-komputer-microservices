package org.tokkom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tokkom.dto.NotificationSetExpiredRequest;
import org.tokkom.model.Notification;
import org.tokkom.service.NotificationConsumer;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Notification>> getNotificationById(@PathVariable Long id) {
        Optional<Notification> notification = notificationConsumer.getById(id);
        return ResponseEntity.ok().body(notification);
    }

    @PostMapping("/setExpired")
    public ResponseEntity<Optional<Notification>> setExpiredNotification(@RequestParam String notifExpire) {
        NotificationSetExpiredRequest notificationSetExpiredRequest = new NotificationSetExpiredRequest();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            notificationSetExpiredRequest = objectMapper.readValue(notifExpire, NotificationSetExpiredRequest.class);

            Boolean setToExpire = notificationConsumer.setExpiration(notificationSetExpiredRequest);

            if (!setToExpire) {
                log.error("Failed set expiration with id : " + notificationSetExpiredRequest.getId());
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }

            Optional<Notification> notification = notificationConsumer.getById(notificationSetExpiredRequest.getId());
            return ResponseEntity.ok().body(notification);
        } catch (Exception e) {
            log.info("Error creating order {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
