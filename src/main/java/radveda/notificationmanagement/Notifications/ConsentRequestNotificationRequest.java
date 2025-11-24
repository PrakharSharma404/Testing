package radveda.notificationmanagement.Notifications;

public record ConsentRequestNotificationRequest(String message, String recipientType, Long recipientId, Long consentRequestId) {
}