package org.tokkom.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.tokkom.repository.NotificationRepository;
import org.tokkom.constant.AppConstants;
import org.tokkom.dto.NotificationRequest;
import org.tokkom.model.Notification;

import java.util.List;

@Service
@Slf4j
public class NotificationConsumerImpl implements NotificationConsumer {

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }
}
