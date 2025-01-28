package org.example.messagedemo.controller;

import org.example.messagedemo.model.ChatMessage;
import org.example.messagedemo.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessage chatMessage) {
        chatService.sendMessage(chatMessage);
    }
}
