package org.example.messagedemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequest {
    private String creatorId;
    private String participantId;
}
