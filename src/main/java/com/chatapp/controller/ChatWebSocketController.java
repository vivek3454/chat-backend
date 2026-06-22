package com.chatapp.websocket;

import com.chatapp.model.Message;
import com.chatapp.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    @MessageMapping("/send")
    public void sendMessage(
            ChatMessageDto chatMessage
    ) {

        Message message = Message.builder()
                .senderId(chatMessage.getSenderId())
                .receiverId(chatMessage.getReceiverId())
                .message(chatMessage.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);

        messagingTemplate.convertAndSend(
                "/topic/messages/" +
                        chatMessage.getReceiverId(),
                message
        );

        messagingTemplate.convertAndSend(
                "/topic/messages/" +
                        chatMessage.getSenderId(),
                message
        );
    }
}