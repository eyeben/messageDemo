package org.example.messagedemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {
    private String roomId;
    private String profileUrl;
    private String opponentName;
    private int newMessageCount;
    private LocalDateTime lastMessageTime;
}
