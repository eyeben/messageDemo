package org.example.messagedemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.messagedemo.dto.ChatRoomResponse;
import org.example.messagedemo.model.ChatMessage;
import org.example.messagedemo.model.ChatRoom;
import org.example.messagedemo.repository.ChatMessageRepository;
import org.example.messagedemo.repository.ChatRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    // 1. 채팅방 목록 조회 (읽지 않은 메시지 개수 포함)
    public List<ChatRoomResponse> getChatRoomsWithUnreadCount(String userId) {
        return chatRoomRepository.findAll().stream()
                .filter(room -> room.getParticipant1Id().equals(userId) || room.getParticipant2Id().equals(userId))
                .map(room -> {
                    String opponentId = room.getParticipant1Id().equals(userId) ? room.getParticipant2Id() : room.getParticipant1Id();
                    int unreadCount = (int) chatMessageRepository.findByChatRoomId(room.getId()).stream()
                            .filter(message -> !message.isRead() && !message.getSenderId().equals(userId))
                            .count();
                    ChatMessage lastMessage = chatMessageRepository.findByChatRoomId(room.getId()).stream()
                            .max(Comparator.comparing(ChatMessage::getTimestamp))
                            .orElse(null);
                    return new ChatRoomResponse(
                            room.getId(),
                            "profile-url-for-" + opponentId,
                            "name-for-" + opponentId,
                            unreadCount,
                            lastMessage != null ? lastMessage.getTimestamp() : null
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * 2. 채팅방 입장 (메시지 조회)
     */
    public List<ChatMessage> getMessages(String chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }

    /**
     * 3. 채팅방 나가기
     */
    public void leaveChatRoom(String roomId, String userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        if (chatRoom.getParticipant1Id().equals(userId)) {
            chatRoom.setParticipant1Active(false);
        } else if (chatRoom.getParticipant2Id().equals(userId)) {
            chatRoom.setParticipant2Active(false);
        }

        if (!chatRoom.isParticipant1Active() && !chatRoom.isParticipant2Active()) {
            chatRoomRepository.delete(chatRoom);
        } else {
            chatRoomRepository.save(chatRoom);
        }
    }

    /**
     * 4. 채팅방 생성
     */
    public ChatRoomResponse createChatRoom(String creatorId, String opponentId) {
        Optional<ChatRoom> existingRoom = chatRoomRepository.findAll().stream()
                .filter(room ->
                        (room.getParticipant1Id().equals(creatorId) && room.getParticipant2Id().equals(opponentId)) ||
                                (room.getParticipant1Id().equals(opponentId) && room.getParticipant2Id().equals(creatorId))
                ).findFirst();

        if (existingRoom.isPresent()) {
            return new ChatRoomResponse(existingRoom.get().getId(), "", "", 0, null);
        }

        ChatRoom chatRoom = new ChatRoom(creatorId, opponentId);
        chatRoom = chatRoomRepository.save(chatRoom);

        return new ChatRoomResponse(chatRoom.getId(), "", "", 0, null);
    }

    /**
     * 5. 메시지 보내기
     */
    public ChatMessage sendMessage(String roomId, String senderId, String content) {
        ChatMessage chatMessage = new ChatMessage(roomId, senderId, content);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    /**
     * 6. 메시지 읽음 처리
     */
    public void markMessagesAsRead(String roomId, String userId) {
        List<ChatMessage> unreadMessages = chatMessageRepository.findByChatRoomId(roomId).stream()
                .filter(message -> !message.isRead() && !message.getSenderId().equals(userId))
                .collect(Collectors.toList());
        unreadMessages.forEach(message -> message.setRead(true));
        chatMessageRepository.saveAll(unreadMessages);
    }
}
