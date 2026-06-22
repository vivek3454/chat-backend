package com.chatapp.repository;

import com.chatapp.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findBySenderIdAndReceiverId(
            String senderId,
            String receiverId
    );

    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
            String senderId,
            String receiverId,
            String senderId2,
            String receiverId2
    );
}