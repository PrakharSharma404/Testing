package radveda.notificationmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import radveda.notificationmanagement.Notifications.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // We mock the Repositories so we don't need a real MySQL DB connection
    @MockBean
    private ChatNotificationRepository chatNotificationRepository;
    @MockBean
    private ConsentRequestNotificationRepository consentRequestNotificationRepository;
    @MockBean
    private OneWayNotificationRepository oneWayNotificationRepository;

    // ==========================================
    // 1. Test Flow: Send Chat Notification (Success)
    // ==========================================
    @Test
    void testSendChatNotification_EndToEnd_Success() throws Exception {
        // 1. Prepare Request Data
        ChatNotificationRequest request = new ChatNotificationRequest(
                "Hello World", "PATIENT", 1L, "Consultation", 100L
        );

        // 2. Perform Request (Controller -> Service -> Repository)
        mockMvc.perform(post("/notifications/sendChatNotification")
                        .header("Authorization", "Bearer valid-token") // Simulates Auth
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // 3. Verify Response
                .andExpect(status().isOk())
                .andExpect(content().string("Notification sent successfully!!"));

        // 4. Verify Side Effect (Service actually called Repository)
        verify(chatNotificationRepository).save(any(ChatNotification.class));
    }

    // ==========================================
    // 2. Test Flow: Send Chat Notification (Auth Fail)
    // ==========================================
    @Test
    void testSendChatNotification_EndToEnd_NoAuth() throws Exception {
        ChatNotificationRequest request = new ChatNotificationRequest(
                "Hello", "PATIENT", 1L, "Chat", 100L
        );

        // Perform request WITHOUT Auth Header
        // Expecting a server error or forbidden status because user is null
        try {
            mockMvc.perform(post("/notifications/sendChatNotification")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // Exception expected if not handled by ControllerAdvice
        }
    }

    // ==========================================
    // 3. Test Flow: Get All Notifications (Data Flow)
    // ==========================================
    @Test
    void testGetAllChatNotifications_Flow() throws Exception {
        // Perform GET request
        mockMvc.perform(get("/notifications/getAllChatNotifications")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk());

        // Verify flow reached repository
        verify(chatNotificationRepository).findByRecipientTypeAndRecipientId("PATIENT", 1L);
    }
}