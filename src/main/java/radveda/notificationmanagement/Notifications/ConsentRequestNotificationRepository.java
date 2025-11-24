package radveda.notificationmanagement.Notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConsentRequestNotificationRepository extends JpaRepository<ConsentRequestNotification, Long> {
    // This is the line we were missing
    List<ConsentRequestNotification> findByRecipientTypeAndRecipientId(String recipientType, Long recipientId);

    // This is for a later step, but let's add it now
    void deleteByRecipientTypeAndRecipientId(String recipientType, Long recipientId);
}