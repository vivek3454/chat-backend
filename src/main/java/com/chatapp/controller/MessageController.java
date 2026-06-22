package com.chatapp.controller;

import com.chatapp.dto.AuthResponse;
import com.chatapp.dto.MessageResponse;
import com.chatapp.model.Message;
import com.chatapp.model.User;
import com.chatapp.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;

    @GetMapping("/{receiverId}")
    public MessageResponse getMessages(
            @PathVariable String receiverId,
            Authentication authentication
    ) {

        User currentUser =
                (User) authentication.getPrincipal();

        List<Message> messages =
                messageRepository
                        .findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                                currentUser.getId(),
                                receiverId,
                                receiverId,
                                currentUser.getId()
                        );

        return MessageResponse.builder()
                .success(true)
                .messages(messages)
                .message("Messages fetched successfully")
                .build();
    }
}