package org.tokkom.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tokkom.dto.NotificationSetExpiredRequest;
import org.tokkom.repository.NotificationRepository;
import org.tokkom.model.Notification;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationConsumerImpl implements NotificationConsumer {

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public Boolean setExpiration(NotificationSetExpiredRequest notificationSetExpiredRequest) {
        try {
            notificationRepository.updateExpirationNotification(notificationSetExpiredRequest.getId(), notificationSetExpiredRequest.getIsExpired());
            log.info("Notification expiration with id : " + notificationSetExpiredRequest.getId() + " successfully updated");
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return false;
    }
}
