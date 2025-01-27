package org.example.messagedemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = "test-topic", groupId = "websocket-consumer-group")
    public void consume(String message) {
        logger.info("Consumed message: {}", message);

        try {
            // WebSocket 클라이언트로 메시지 전송
            simpMessagingTemplate.convertAndSend("/topic/messages", Map.of("content", message));
            logger.info("Message sent to WebSocket clients: {}", message);
        } catch (Exception e) {
            logger.error("Failed to send message to WebSocket clients", e);
        }
    }
}
