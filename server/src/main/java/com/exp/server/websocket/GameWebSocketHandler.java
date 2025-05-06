package com.exp.server.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.exp.server.model.MatchModel;
import com.exp.server.model.PlayerModel;
import com.exp.server.repository.MatchRepository;
import com.exp.server.repository.PlayerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private PlayerRepository playerRepository;

    private static final Map<String, WebSocketSession> tokenSessionMap = new ConcurrentHashMap<>();
    private static final Map<String, String> sessionIdToTokenMap = new ConcurrentHashMap<>();

    @Autowired
    private MatchRepository matchRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    //連線
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = getQueryParam(session, "token");

        if (token == null || token.isBlank()) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("缺少 token"));
            return;
        }

        PlayerModel player = playerRepository.findByToken(token);
        if (player == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("無效 token"));
            return;
        }

        System.out.println("玩家已通過驗證: " + player.getUserName());

        tokenSessionMap.put(token, session);
        sessionIdToTokenMap.put(session.getId(), token);
    }

    //斷線
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String token = sessionIdToTokenMap.remove(session.getId());
        if (token != null) {
            tokenSessionMap.remove(token);
        }

        System.out.println("玩家離線: " + session.getId());
    }


    private String getQueryParam(WebSocketSession session, String key) {
        String query = session.getUri().getQuery();
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(key)) return pair[1];
        }
        return null;
    }

    public static void sendToToken(String token, String jsonMessage) {
        WebSocketSession session = tokenSessionMap.get(token);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (Exception e) {
                System.out.println("傳送給 " + token + " 失敗：" + e.getMessage());
            }
        } else {
            System.out.println("找不到 token 對應的 session 或連線已關閉：" + token);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode json = mapper.readTree(message.getPayload());
        String type = json.get("type").asText();

        if ("shot".equals(type)) {
            String matchId = json.get("matchId").asText();
            MatchModel match = matchRepository.findById(matchId).orElse(null);
            if (match == null) return;

            // 判斷是否輪到這個玩家（token 取自當前 session）
            String senderToken = getQueryParam(session, "token");
            if (!match.getCurrentPlayerId().equals(senderToken)) {
                System.out.println("⚠️ 不合法操作：不是你的回合！");
                String denyMsg = "{\"type\":\"error\",\"message\":\"不是你的回合\"}";
                sendToToken(senderToken, denyMsg);
                return;
            }

            // 判斷 chessId 是否是自己的棋子（暫略）

            // 模擬物理（略）... 你可以呼叫 physicsEngine.simulateShot(...)

            // 換人
            String nextTurn = match.getCurrentPlayerId().equals(match.getPlayer1Id())
                ? match.getPlayer2Id() : match.getPlayer1Id();

            match.setCurrentPlayerId(nextTurn);
            matchRepository.save(match);

            // 廣播 nextTurn 給雙方
            String msgToP1 = String.format(
                "{\"type\":\"turn_update\",\"yourTurn\":%s}",
                match.getPlayer1Id().equals(nextTurn)
            );
            String msgToP2 = String.format(
                "{\"type\":\"turn_update\",\"yourTurn\":%s}",
                match.getPlayer2Id().equals(nextTurn)
            );

            sendToToken(match.getPlayer1Id(), msgToP1);
            sendToToken(match.getPlayer2Id(), msgToP2);

            System.out.println("收到射擊，切換到: " + nextTurn);
        }
    }

    
}


