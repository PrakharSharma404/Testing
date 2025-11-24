package radveda.notificationmanagement;



import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;

import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpClientErrorException;

import org.springframework.web.client.RestTemplate;

import radveda.notificationmanagement.Notifications.*;

import radveda.notificationmanagement.exception.NotificationNotFoundException;

import radveda.notificationmanagement.exception.UnauthorisedUserException;



import java.util.*;



@Service

@RequiredArgsConstructor

public class NotificationService implements NotificationServiceInterface {

    private final ChatNotificationRepository chatNotificationRepository;

    private final ConsentRequestNotificationRepository consentRequestNotificationRepository;

    private final OneWayNotificationRepository oneWayNotificationRepository;



    @Override

    public List<ChatNotification> findAllChatNotificationsByRecipient(String recipientType, Long recipientId) {

        return chatNotificationRepository.findByRecipientTypeAndRecipientId(recipientType, recipientId);

    }



    @Override

    public List<ConsentRequestNotification> findAllConsentRequestNotificationsByRecipient(String recipientType, Long recipientId) {

        return consentRequestNotificationRepository.findByRecipientTypeAndRecipientId(recipientType, recipientId);

    }



    @Override

    public List<OneWayNotification> findAllOneWayNotificationsByRecipient(String recipientType, Long recipientId) {

        return oneWayNotificationRepository.findByRecipientTypeAndRecipientId(recipientType, recipientId);

    }



    @Override

    public ChatNotification findChatNotificationById(Long Id, User currentUser) {

        return chatNotificationRepository.findById(Id)

                .filter(chatNotif -> Objects.equals(currentUser.getId(), chatNotif.getRecipientId()) && Objects.equals(currentUser.getType(), chatNotif.getRecipientType()))

                .orElseThrow(() -> new NotificationNotFoundException("Couldn't find a notification with the given ID or permission denied"));

    }



    @Override

    public ConsentRequestNotification findConsentRequestNotificationById(Long Id, User currentUser) {

        return consentRequestNotificationRepository.findById(Id)

                .filter(consentNotif -> Objects.equals(currentUser.getId(), consentNotif.getRecipientId()) && Objects.equals(currentUser.getType(), consentNotif.getRecipientType()))

                .orElseThrow(() -> new NotificationNotFoundException("Couldn't find a notification with the given ID or permission denied"));

    }



    @Override

    public OneWayNotification findOneWayNotificationById(Long Id, User currentUser) {

        return oneWayNotificationRepository.findById(Id)

                .filter(oneWayNotif -> Objects.equals(currentUser.getId(), oneWayNotif.getRecipientId()) && Objects.equals(currentUser.getType(), oneWayNotif.getRecipientType()))

                .orElseThrow(() -> new NotificationNotFoundException("Couldn't find a notification with the given ID or permission denied"));

    }



    @Override

    public String sendChatNotificationToRecipient(String message, String recipientType, Long recipientId, String chatType, Long chatId) {

        ChatNotification chatNotif = new ChatNotification();

        chatNotif.setMessage(message);

        chatNotif.setRecipientType(recipientType);

        chatNotif.setRecipientId(recipientId);

        chatNotif.setChatId(chatId);

        chatNotif.setChatType(chatType);

        chatNotificationRepository.save(chatNotif);

        return "Notification sent successfully!!";

    }



    @Override

    public String sendConsentRequestNotificationToRecipient(String message, String recipientType, Long recipientId, Long consentRequestId) {

        ConsentRequestNotification consReqNotif = new ConsentRequestNotification();

        consReqNotif.setMessage(message);

        consReqNotif.setRecipientType(recipientType);

        consReqNotif.setRecipientId(recipientId);

        consReqNotif.setConsentRequestId(consentRequestId);

        consentRequestNotificationRepository.save(consReqNotif);

        return "Notification sent successfully!!";

    }



    @Override

    public String sendOneWayNotificationToRecipient(String message, String recipientType, Long recipientId) {

        OneWayNotification oneWayNotif = new OneWayNotification();

        oneWayNotif.setMessage(message);

        oneWayNotif.setRecipientType(recipientType);

        oneWayNotif.setRecipientId(recipientId);

        oneWayNotificationRepository.save(oneWayNotif);

        return "Notification sent successfully!!";

    }



// ... delete methods ... (We can add these back later if needed)



// Simplified methods for the tutorial

    @Override

    public String deleteChatNotificationOfRecipient(Long Id, User currentUser) { return "Not implemented yet"; }

    @Override

    public String deleteConsentRequestNotificationOfRecipient(Long Id, User currentUser) { return "Not implemented yet"; }

    @Override

    public String deleteOneWayNotificationOfRecipient(Long Id, User currentUser) { return "Not implemented yet"; }

    @Override

    public String deleteAllChatNotificationsOfRecipient(String recipientType, Long recipientId) { return "Not implemented yet"; }

    @Override

    public String deleteAllConsentRequestNotificationsOfRecipient(String recipientType, Long recipientId) { return "Not implemented yet"; }

    @Override

    public String deleteAllOneWayNotificationsOfRecipient(String recipientType, Long recipientId) { return "Not implemented yet"; }



    @Override

    public User authenticate(String authorizationHeader) {

// This is a placeholder for a real authentication service.

        if (authorizationHeader != null) {

            User user = new User();

            user.setId(1L); // Example user ID

            user.setType("PATIENT"); // Example user type

            return user;

        }

        return null;

    }



    @Override

    public boolean isRecipientValid(String recipientType, Long recipientId, String authorizationHeader) {

        return true; // Placeholder

    }



    @Override

    public boolean isChatValid(String chatType, Long chatId, String authorizationHeader) {

        return true; // Placeholder

    }



    @Override

    public boolean isConsentRequestValid(Long consentRequestId, String authorizationHeader) {

        return true; // Placeholder

    }

}