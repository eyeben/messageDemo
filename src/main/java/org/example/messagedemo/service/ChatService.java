package org.example.messagedemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.messagedemo.dto.ChatRoomResponse;
import org.example.messagedemo.model.ChatMessage;
import org.example.messagedemo.model.ChatRoom;
import org.example.messagedemo.model.ChatRoomStatus;
import org.example.messagedemo.repository.ChatMessageRepository;
import org.example.messagedemo.repository.ChatRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic,
                       ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatRoomResponse createChatRoom(String creatorId, String participantId) {
        Optional<ChatRoom> existingRoom = chatRoomRepository.findAll().stream()
                .filter(room ->
                        (room.getParticipant1Id().equals(creatorId) && room.getParticipant2Id().equals(participantId)) ||
                                (room.getParticipant1Id().equals(participantId) && room.getParticipant2Id().equals(creatorId)))
                .findFirst();

        if (existingRoom.isPresent()) {
            ChatRoom room = existingRoom.get();
            return new ChatRoomResponse(
                    room.getId(), room.getParticipant1Id(), room.getParticipant2Id(),
                    room.getStatus1() == ChatRoomStatus.ACTIVE,
                    room.getStatus2() == ChatRoomStatus.ACTIVE
            );
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setParticipant1Id(creatorId);
        chatRoom.setParticipant2Id(participantId);
        chatRoom.setStatus1(ChatRoomStatus.ACTIVE);
        chatRoom.setStatus2(ChatRoomStatus.ACTIVE);
        chatRoom = chatRoomRepository.save(chatRoom);

        return new ChatRoomResponse(
                chatRoom.getId(), chatRoom.getParticipant1Id(), chatRoom.getParticipant2Id(),
                true, true
        );
    }

    public List<ChatRoomResponse> getChatRooms(String userId) {
        return chatRoomRepository.findAll().stream()
                .filter(room -> room.getParticipant1Id().equals(userId) || room.getParticipant2Id().equals(userId))
                .map(room -> new ChatRoomResponse(
                        room.getId(),
                        room.getParticipant1Id(),
                        room.getParticipant2Id(),
                        room.getStatus1() == ChatRoomStatus.ACTIVE,
                        room.getStatus2() == ChatRoomStatus.ACTIVE
                ))
                .collect(Collectors.toList());
    }

    public List<ChatMessage> getMessages(String chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }

    public void sendMessage(ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessageRepository.save(chatMessage); // MongoDB 저장
        redisTemplate.convertAndSend(topic.getTopic(), chatMessage); // Redis Pub/Sub
    }

    public void leaveChatRoom(String roomId, String userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        if (chatRoom.getParticipant1Id().equals(userId)) {
            chatRoom.setStatus1(ChatRoomStatus.INACTIVE);
        } else if (chatRoom.getParticipant2Id().equals(userId)) {
            chatRoom.setStatus2(ChatRoomStatus.INACTIVE);
        }

        if (chatRoom.getStatus1() == ChatRoomStatus.INACTIVE && chatRoom.getStatus2() == ChatRoomStatus.INACTIVE) {
            chatRoomRepository.delete(chatRoom);
        } else {
            chatRoomRepository.save(chatRoom);
        }
    }
}
