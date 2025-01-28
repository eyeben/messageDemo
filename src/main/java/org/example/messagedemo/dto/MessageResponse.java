package org.example.messagedemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private String chatRoomId;
    private String senderId;
    private String content;
    private String timestamp;
}
