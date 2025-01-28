package org.example.messagedemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatRoomResponse {
    private String chatRoomId;
    private String participant1Id;
    private String participant2Id;
    private boolean participant1Active;
    private boolean participant2Active;
}
