package org.example.messagedemo.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisher {
    private final RabbitTemplate rabbitTemplate;
    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String queueName, String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
