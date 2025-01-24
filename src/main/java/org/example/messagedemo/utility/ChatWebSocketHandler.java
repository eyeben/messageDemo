package org.example.messagedemo.utility;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        for (WebSocketSession s: sessions){
            s.sendMessage(new TextMessage("User: " + payload));
        }
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); // 연결된 세션 저장
        session.sendMessage(new TextMessage("Welcome to the chat!")); // 클라이언트에 환영 메시지 전송
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
    }

}
