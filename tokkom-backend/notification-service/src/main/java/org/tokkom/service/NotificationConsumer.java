package org.tokkom.service;

import org.tokkom.dto.NotificationRequest;
import org.tokkom.model.Notification;

import java.util.List;

public interface NotificationConsumer {

    public List<Notification> getAll();
}
