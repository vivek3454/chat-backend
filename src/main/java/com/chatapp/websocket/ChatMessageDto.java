package com.chatapp.websocket;

import lombok.Data;

@Data
public class ChatMessageDto {

    private String senderId;

    private String receiverId;

    private String message;
}