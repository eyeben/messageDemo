package org.example.messagedemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;
    private String chatRoomId;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;
}
