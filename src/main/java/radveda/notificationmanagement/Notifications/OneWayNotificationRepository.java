package radveda.notificationmanagement.Notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OneWayNotificationRepository extends JpaRepository<OneWayNotification, Long> {
    // This is the line we were missing
    List<OneWayNotification> findByRecipientTypeAndRecipientId(String recipientType, Long recipientId);

    // This is for a later step, but let's add it now
    void deleteByRecipientTypeAndRecipientId(String recipientType, Long recipientId);
}