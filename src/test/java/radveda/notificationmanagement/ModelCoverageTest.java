package radveda.notificationmanagement;

import org.junit.jupiter.api.Test;
import radveda.notificationmanagement.Notifications.*;
import radveda.notificationmanagement.exception.*;

import static org.junit.jupiter.api.Assertions.*;

class ModelCoverageTest {

    @Test
    void testEntityGettersAndSetters() {
        // 1. Test ChatNotification
        ChatNotification chat = new ChatNotification();
        chat.setId(1L);
        chat.setChatType("Private");
        chat.setChatId(100L);
        chat.setMessage("Hello");
        chat.setRecipientType("PATIENT");
        chat.setRecipientId(5L);

        assertEquals(1L, chat.getId());
        assertEquals("Private", chat.getChatType());
        assertEquals(100L, chat.getChatId());
        assertEquals("Hello", chat.getMessage());
        assertEquals("PATIENT", chat.getRecipientType());
        assertEquals(5L, chat.getRecipientId());

        // Test AllArgsConstructor
        ChatNotification chat2 = new ChatNotification(1L, "Group", 200L);
        assertNotNull(chat2);
    }

    @Test
    void testConsentNotification() {
        ConsentRequestNotification consent = new ConsentRequestNotification();
        consent.setId(2L);
        consent.setConsentRequestId(99L);

        assertEquals(2L, consent.getId());
        assertEquals(99L, consent.getConsentRequestId());

        // Test AllArgsConstructor
        ConsentRequestNotification consent2 = new ConsentRequestNotification(2L, 99L);
        assertNotNull(consent2);
    }

    @Test
    void testOneWayNotification() {
        OneWayNotification oneWay = new OneWayNotification();
        oneWay.setId(3L);
        assertEquals(3L, oneWay.getId());

        // Test AllArgsConstructor
        OneWayNotification oneWay2 = new OneWayNotification(3L);
        assertNotNull(oneWay2);
    }

    @Test
    void testUser() {
        User user = new User();
        user.setId(10L);
        user.setType("DOCTOR");

        assertEquals(10L, user.getId());
        assertEquals("DOCTOR", user.getType());
    }

    @Test
    void testRecords() {
        // Records typically have implicit tests, but we can call them to be sure
        ChatNotificationRequest req1 = new ChatNotificationRequest("Msg", "P", 1L, "C", 2L);
        assertEquals("Msg", req1.message());

        ConsentRequestNotificationRequest req2 = new ConsentRequestNotificationRequest("Msg", "D", 2L, 50L);
        assertEquals("Msg", req2.message());

        OneWayNotificationRequest req3 = new OneWayNotificationRequest("Alert", "A", 3L);
        assertEquals("Alert", req3.message());
    }

    @Test
    void testExceptions() {
        // Instantiate all exceptions to cover their constructors
        assertNotNull(new InvalidChatException("Error"));
        assertNotNull(new InvalidConsentRequestException("Error"));
        assertNotNull(new NotificationNotFoundException("Error"));
        assertNotNull(new RecipientNotFoundException("Error"));
        assertNotNull(new UnauthorisedUserException("Error"));
    }
}