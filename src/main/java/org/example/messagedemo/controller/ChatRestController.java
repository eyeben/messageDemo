package org.example.messagedemo.controller;

import org.example.messagedemo.dto.ChatRoomRequest;
import org.example.messagedemo.dto.ChatRoomResponse;
import org.example.messagedemo.model.ChatMessage;
import org.example.messagedemo.model.ChatRoom;
import org.example.messagedemo.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/chats")
public class ChatRestController {

    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatRoomResponse createChatRoom(@RequestBody ChatRoomRequest request) {
        return chatService.createChatRoom(request.getCreatorId(), request.getParticipantId());
    }

    @GetMapping
    public List<ChatRoomResponse> getChatRooms(@RequestParam String userId) {
        return chatService.getChatRooms(userId);
    }

    @GetMapping("/{roomId}")
    public List<ChatMessage> getMessages(@PathVariable String roomId) {
        return chatService.getMessages(roomId);
    }

    @PostMapping("/{roomId}")
    public void sendMessage(@PathVariable String roomId, @RequestBody ChatMessage chatMessage) {
        chatMessage.setChatRoomId(roomId);
        chatService.sendMessage(chatMessage);
    }

    @DeleteMapping("/{roomId}")
    public void leaveChatRoom(@PathVariable String roomId, @RequestParam String userId) {
        chatService.leaveChatRoom(roomId, userId);
    }
}
