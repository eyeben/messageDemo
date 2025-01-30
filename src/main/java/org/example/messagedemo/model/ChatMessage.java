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
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    private String id;
    private String chatRoomId;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;

    public ChatMessage(String chatRoomId, String senderId, String content) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = LocalDateTime.now();  // 현재 시간 자동 설정
        this.isRead = false; // 기본값: 안 읽음 상태
    }
}
