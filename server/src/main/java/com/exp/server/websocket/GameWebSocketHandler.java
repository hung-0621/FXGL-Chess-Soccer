package com.exp.server.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.exp.server.service.PhysicsEngine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final PhysicsEngine physicsEngine = new PhysicsEngine();

    // 儲存所有連線的玩家（可依照房間編碼分群）
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 可加登入邏輯、token驗證等
        System.out.println("玩家已連線: " + session.getId());
        sessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("玩家離線: " + session.getId());
        sessions.remove(session.getId());
    }

}
