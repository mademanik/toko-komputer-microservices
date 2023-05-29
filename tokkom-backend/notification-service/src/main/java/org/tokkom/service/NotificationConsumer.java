package org.tokkom.service;

import org.tokkom.dto.NotificationSetExpiredRequest;
import org.tokkom.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationConsumer {

    public List<Notification> getAll();

    public Boolean setExpiration(NotificationSetExpiredRequest notificationSetExpiredRequest);

    public Optional<Notification> getById(Long id);
}
