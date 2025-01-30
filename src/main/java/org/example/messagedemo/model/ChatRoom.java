package org.example.messagedemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_rooms")
public class ChatRoom {
    @Id
    private String id;
    private String participant1Id;
    private String participant2Id;
    private boolean participant1Active;
    private boolean participant2Active;

    public ChatRoom(String participant1Id, String participant2Id) {
        this.participant1Id = participant1Id;
        this.participant2Id = participant2Id;
        this.participant1Active = false; // 기본값: 비활성화 상태
        this.participant2Active = false; // 기본값: 비활성화 상태
    }
}

