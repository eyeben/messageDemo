package org.example.messagedemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    private String senderId;
    private String content;
}
