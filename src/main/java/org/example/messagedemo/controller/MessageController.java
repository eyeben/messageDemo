package org.example.messagedemo.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.example.messagedemo.service.KafkaProducerService;



@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final KafkaProducerService kafkaProducerService;

    public MessageController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @MessageMapping("/send")
    public void handleMessage(@Payload String message) {
        logger.info("Received message from WebSocket: {}", message);

        // Kafka로 메시지 전송
        kafkaProducerService.sendMessage("test-topic", message);
    }
}

