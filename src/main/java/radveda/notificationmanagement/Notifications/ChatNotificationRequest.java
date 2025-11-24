package radveda.notificationmanagement.Notifications;

public record ChatNotificationRequest(String message, String recipientType, Long recipientId, String chatType, Long chatId) {
}