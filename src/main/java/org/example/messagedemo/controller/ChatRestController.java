package org.example.messagedemo.controller;

import org.example.messagedemo.dto.ApiResponse;
import org.example.messagedemo.dto.MessageRequest;
import org.example.messagedemo.dto.MessageResponse;
import org.example.messagedemo.dto.ChatRoomRequest;
import org.example.messagedemo.dto.ChatRoomResponse;
import org.example.messagedemo.model.ChatMessage;
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

    /**
     * 1. 채팅방 목록 조회 (GET /api/chats)
     * - 사용자가 참여한 채팅방 목록 조회
     * - 읽지 않은 메시지 개수 포함
     */
    @GetMapping
    public ApiResponse<List<ChatRoomResponse>> getChatRooms(@RequestParam String userId) {
        return new ApiResponse<>("S0000", "Success", chatService.getChatRoomsWithUnreadCount(userId));
    }

    /**
     * 2. 채팅방 입장 (GET /api/chats/{roomId})
     * - 해당 채팅방의 메시지 조회
     * - 읽지 않은 메시지를 읽음 처리
     */
    @GetMapping("/{roomId}")
    public ApiResponse<List<ChatMessage>> getMessages(@PathVariable String roomId, @RequestParam String userId) {
        chatService.markMessagesAsRead(roomId, userId); // 메시지 읽음 처리
        return new ApiResponse<>("S0000", "Success", chatService.getMessages(roomId));
    }

    /**
     * 3. 채팅방 나가기 (DELETE /api/chats/{roomId})
     * - 사용자가 채팅방을 나가면 채팅방 상태 변경 또는 삭제
     */
    @DeleteMapping("/{roomId}")
    public ApiResponse<Void> leaveChatRoom(@PathVariable String roomId, @RequestParam String userId) {
        chatService.leaveChatRoom(roomId, userId);
        return new ApiResponse<>("S0000", "User left the chat room", null);
    }

    /**
     * 4. 채팅방 생성 (POST /api/chats)
     * - 두 사용자가 새로운 채팅방을 생성
     */
    @PostMapping
    public ApiResponse<ChatRoomResponse> createChatRoom(@RequestParam String userId, @RequestBody ChatRoomRequest request) {
        return new ApiResponse<>("S0000", "Chat room created", chatService.createChatRoom(userId, request.getOpponentId()));
    }

    /**
     * ✅ 5. 메시지 보내기 (POST /api/chats/{roomId})
     * - 사용자가 채팅방에서 메시지를 보냄
     */
    @PostMapping("/{roomId}")
    public ApiResponse<MessageResponse> sendMessage(@PathVariable String roomId,
                                                    @RequestParam String userId,
                                                    @RequestBody MessageRequest request) {
        ChatMessage chatMessage = chatService.sendMessage(roomId, userId, request.getContent());
        return new ApiResponse<>("S0000", "Message sent", new MessageResponse(chatMessage.getContent(), chatMessage.getTimestamp()));
    }
}

