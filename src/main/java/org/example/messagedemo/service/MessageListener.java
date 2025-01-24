package org.example.messagedemo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {
    @RabbitListener(queues = "chat.queue")
    public void receiveMessage(String message) {
        System.out.println("RECEIVED"+message);
    }
}
