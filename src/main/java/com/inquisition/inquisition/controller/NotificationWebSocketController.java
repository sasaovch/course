package com.inquisition.inquisition.controller;

import java.text.SimpleDateFormat;

import com.inquisition.inquisition.model.notification.entity.Notification;
import com.inquisition.inquisition.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationWebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
//    @Autowired private ChatMessag/**/eService chatMessageService;
//    @Autowired private ChatRoomService chatRoomService;

//    @MessageMapping("/")
//    public void processMessage(@Payload ChatMessage chatMessage) {
//        var chatId = chatRoomService
//                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
//        chatMessage.setChatId(chatId.get());
//
//        ChatMessage saved = chatMessageService.save(chatMessage);
//
//        messagingTemplate.convertAndSendToUser(
//                chatMessage.getRecipientId(),"/queue/messages",
//                new ChatNotification(
//                        saved.getId(),
//                        saved.getSenderId(),
//                        saved.getSenderName()));
//    }
    @Autowired
    private SessionRegistry sessionRegistry;

    public void sendNotificationToAllUsers(Notification notification) {
        messagingTemplate.convertAndSend("/topic/greetings", notification);
        sessionRegistry.getAllPrincipals().forEach(principal -> {
            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
//                messagingTemplate.convertAndSendToUser(username, "/queue/notification", notification);
            }
        })
        ;
    }

//    @MessageMapping("/user")
//    @SendTo("/app/notification")
//    public String send(Message message) throws Exception {
//        String time = new SimpleDateFormat("HH:mm").format(new Date());
//        return Messages.ERROR_WHILE_HANDLE_REQUEST;
//    }


    // This can be called from any method inside your Controller to send a notification to clients.
    // This can be called from any method inside your Controller to send a notification to specific client.
    public void sendNotification1(Notification notification, Integer personId) {
        this.messagingTemplate.convertAndSend("/topic/" + personId + "/notification", notification);
    }


    public void sendNotification(Notification notification, Integer personId) {
        messagingTemplate.convertAndSendToUser(
                personId.toString(), "/notification",
                notification)
        ;
    }
}
