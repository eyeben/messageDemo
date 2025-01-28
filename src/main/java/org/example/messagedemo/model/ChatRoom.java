package org.example.messagedemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_rooms")
public class ChatRoom {
    @Id
    private String id; // MongoDB에서 자동 생성
    private String participant1Id;
    private String participant2Id;
    private boolean participant1Active; // WebSocket으로 관리
    private boolean participant2Active; // WebSocket으로 관리

    public ChatRoom(String participant1Id, String participant2Id) {
        this.participant1Id = participant1Id;
        this.participant2Id = participant2Id;
        this.participant1Active = false; // 기본 상태는 INACTIVE
        this.participant2Active = false; // 기본 상태는 INACTIVE
    }
}

