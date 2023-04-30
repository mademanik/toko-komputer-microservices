package org.tokkom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.tokkom.constant.AppConstants;
import org.tokkom.dto.NotificationRequest;
import org.tokkom.model.Notification;
import org.tokkom.repository.NotificationRepository;

@SpringBootApplication
@EnableEurekaClient
@Slf4j
public class NotificationServerApplication {
    @Autowired
    NotificationRepository notificationRepository;

    public static void main(String[] args) {
        SpringApplication.run(NotificationServerApplication.class, args);
    }

    @KafkaListener(topics = AppConstants.TOPIC_NAME)
    public void init(NotificationRequest notificationRequest) {
        log.info("Get new incoming messages from producer with topic {}", AppConstants.TOPIC_NAME);
        Notification notification = Notification.builder()
                .message(notificationRequest.getMessage())
                .serviceName(notificationRequest.getServiceName())
                .createdAt(notificationRequest.getCreatedAt())
                .isExpired(0)
                .build();
        notificationRepository.save(notification);
    }
}
