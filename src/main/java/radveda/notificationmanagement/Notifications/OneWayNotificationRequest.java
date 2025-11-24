package radveda.notificationmanagement.Notifications;

public record OneWayNotificationRequest(String message, String recipientType, Long recipientId) {
}