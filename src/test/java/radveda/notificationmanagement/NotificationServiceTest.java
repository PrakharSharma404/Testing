package radveda.notificationmanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import radveda.notificationmanagement.Notifications.*;
import radveda.notificationmanagement.exception.NotificationNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private ChatNotificationRepository chatNotificationRepository;
    @Mock
    private ConsentRequestNotificationRepository consentRequestNotificationRepository;
    @Mock
    private OneWayNotificationRepository oneWayNotificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    // ==========================================
    // 1. Tests for Chat Notifications
    // ==========================================

    @Test
    void testFindAllChatNotificationsByRecipient() {
        // Arrange
        ChatNotification notification = new ChatNotification();
        when(chatNotificationRepository.findByRecipientTypeAndRecipientId("PATIENT", 1L))
                .thenReturn(Collections.singletonList(notification));

        // Act
        List<ChatNotification> results = notificationService.findAllChatNotificationsByRecipient("PATIENT", 1L);

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(chatNotificationRepository).findByRecipientTypeAndRecipientId("PATIENT", 1L);
    }

    @Test
    void testFindChatNotificationById_Success() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setType("PATIENT");

        ChatNotification notification = new ChatNotification();
        notification.setRecipientId(1L);
        notification.setRecipientType("PATIENT");

        when(chatNotificationRepository.findById(100L)).thenReturn(Optional.of(notification));

        // Act
        ChatNotification result = notificationService.findChatNotificationById(100L, currentUser);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getRecipientId());
    }

    @Test
    void testFindChatNotificationById_NotFound_InDB() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setType("PATIENT");

        when(chatNotificationRepository.findById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificationNotFoundException.class, () ->
                notificationService.findChatNotificationById(100L, currentUser)
        );
    }

    @Test
    void testFindChatNotificationById_WrongUser_IdMismatch() {
        // Arrange: Current user is ID 2, but notification belongs to ID 1
        User currentUser = new User();
        currentUser.setId(2L);
        currentUser.setType("PATIENT");

        ChatNotification notification = new ChatNotification();
        notification.setRecipientId(1L);
        notification.setRecipientType("PATIENT");

        when(chatNotificationRepository.findById(100L)).thenReturn(Optional.of(notification));

        // Act & Assert: Should fail because of the filter() logic
        assertThrows(NotificationNotFoundException.class, () ->
                notificationService.findChatNotificationById(100L, currentUser)
        );
    }

    @Test
    void testFindChatNotificationById_WrongUser_TypeMismatch() {
        // Arrange: Current user is DOCTOR, but notification belongs to PATIENT
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setType("DOCTOR");

        ChatNotification notification = new ChatNotification();
        notification.setRecipientId(1L);
        notification.setRecipientType("PATIENT");

        when(chatNotificationRepository.findById(100L)).thenReturn(Optional.of(notification));

        // Act & Assert
        assertThrows(NotificationNotFoundException.class, () ->
                notificationService.findChatNotificationById(100L, currentUser)
        );
    }

    @Test
    void testSendChatNotificationToRecipient() {
        // Act
        String response = notificationService.sendChatNotificationToRecipient("Msg", "PATIENT", 1L, "Chat", 55L);

        // Assert
        assertEquals("Notification sent successfully!!", response);
        verify(chatNotificationRepository).save(any(ChatNotification.class));
    }

    // ==========================================
    // 2. Tests for Consent Notifications
    // ==========================================

    @Test
    void testFindAllConsentRequestNotifications() {
        when(consentRequestNotificationRepository.findByRecipientTypeAndRecipientId("DOCTOR", 2L))
                .thenReturn(Collections.emptyList());

        List<ConsentRequestNotification> res = notificationService.findAllConsentRequestNotificationsByRecipient("DOCTOR", 2L);

        assertTrue(res.isEmpty());
    }

    @Test
    void testFindConsentRequestNotificationById_Success() {
        User currentUser = new User();
        currentUser.setId(5L);
        currentUser.setType("DOCTOR");

        ConsentRequestNotification notif = new ConsentRequestNotification();
        notif.setRecipientId(5L);
        notif.setRecipientType("DOCTOR");

        when(consentRequestNotificationRepository.findById(200L)).thenReturn(Optional.of(notif));

        ConsentRequestNotification result = notificationService.findConsentRequestNotificationById(200L, currentUser);
        assertNotNull(result);
    }

    @Test
    void testSendConsentRequestNotification() {
        String response = notificationService.sendConsentRequestNotificationToRecipient("Request", "DOCTOR", 5L, 99L);

        assertEquals("Notification sent successfully!!", response);
        verify(consentRequestNotificationRepository).save(any(ConsentRequestNotification.class));
    }

    // ==========================================
    // 3. Tests for OneWay Notifications
    // ==========================================

    @Test
    void testFindAllOneWayNotifications() {
        when(oneWayNotificationRepository.findByRecipientTypeAndRecipientId("ADMIN", 1L))
                .thenReturn(Collections.emptyList());
        assertTrue(notificationService.findAllOneWayNotificationsByRecipient("ADMIN", 1L).isEmpty());
    }

    @Test
    void testFindOneWayNotificationById_Success() {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setType("ADMIN");

        OneWayNotification notif = new OneWayNotification();
        notif.setRecipientId(1L);
        notif.setRecipientType("ADMIN");

        when(oneWayNotificationRepository.findById(300L)).thenReturn(Optional.of(notif));

        OneWayNotification result = notificationService.findOneWayNotificationById(300L, currentUser);
        assertNotNull(result);
    }

    @Test
    void testSendOneWayNotification() {
        String response = notificationService.sendOneWayNotificationToRecipient("Alert", "ADMIN", 1L);

        assertEquals("Notification sent successfully!!", response);
        verify(oneWayNotificationRepository).save(any(OneWayNotification.class));
    }

    // ==========================================
    // 4. Test Authenticate (Placeholder Logic)
    // ==========================================

    @Test
    void testAuthenticate_Success() {
        User user = notificationService.authenticate("Bearer token123");
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("PATIENT", user.getType());
    }

    @Test
    void testAuthenticate_Failure() {
        User user = notificationService.authenticate(null);
        assertNull(user);
    }

    // ==========================================
    // 5. Test Validation Placeholders (Coverage)
    // ==========================================

    @Test
    void testValidationsReturnsTrue() {
        assertTrue(notificationService.isRecipientValid("Type", 1L, "Auth"));
        assertTrue(notificationService.isChatValid("Type", 1L, "Auth"));
        assertTrue(notificationService.isConsentRequestValid(1L, "Auth"));
    }
}