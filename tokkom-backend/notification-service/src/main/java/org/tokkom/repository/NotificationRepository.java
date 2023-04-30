package org.tokkom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tokkom.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update notifications set is_expired = :isExpired where id = :id", nativeQuery = true)
    void updateExpirationNotification(@Param("id") Long id, @Param("isExpired") Integer isExpired);
}
