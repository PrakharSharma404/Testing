package radveda.notificationmanagement;

import radveda.notificationmanagement.Notifications.ChatNotification;
import radveda.notificationmanagement.Notifications.ConsentRequestNotification;
import radveda.notificationmanagement.Notifications.OneWayNotification;
import java.util.List;

public interface NotificationServiceInterface {
    List<ChatNotification> findAllChatNotificationsByRecipient(String recipientType, Long recipientId);
    List<ConsentRequestNotification> findAllConsentRequestNotificationsByRecipient(String recipientType, Long recipientId);
    List<OneWayNotification> findAllOneWayNotificationsByRecipient(String recipientType, Long recipientId);
    ChatNotification findChatNotificationById(Long Id, User currentUser);
    ConsentRequestNotification findConsentRequestNotificationById(Long Id, User currentUser);
    OneWayNotification findOneWayNotificationById(Long Id, User currentUser);
    String sendChatNotificationToRecipient(String message, String recipientType, Long recipientId, String chatType, Long chatId);
    String sendConsentRequestNotificationToRecipient(String message, String recipientType, Long recipientId, Long consentRequestId);
    String sendOneWayNotificationToRecipient(String message, String recipientType, Long recipientId);
    String deleteChatNotificationOfRecipient(Long Id, User currentUser);
    String deleteConsentRequestNotificationOfRecipient(Long Id, User currentUser);
    String deleteOneWayNotificationOfRecipient(Long Id, User currentUser);
    String deleteAllChatNotificationsOfRecipient(String recipientType, Long recipientId);
    String deleteAllConsentRequestNotificationsOfRecipient(String recipientType, Long recipientId);
    String deleteAllOneWayNotificationsOfRecipient(String recipientType, Long recipientId);
    User authenticate(String authorizationHeader);
    boolean isRecipientValid(String recipientType, Long recipientId, String authorizationHeader);
    boolean isChatValid(String chatType, Long chatId, String authorizationHeader);
    boolean isConsentRequestValid(Long consentRequestId, String authorizationHeader);
}