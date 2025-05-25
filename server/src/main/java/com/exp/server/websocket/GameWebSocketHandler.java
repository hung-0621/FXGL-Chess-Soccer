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
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    private static final Map<String, WebSocketSession> tokenSessionMap = new ConcurrentHashMap<>();
    private static final Map<String, String> sessionIdToTokenMap = new ConcurrentHashMap<>();
    // private static final Map<String, LocalDateTime> disconnectTimeMap = new
    // ConcurrentHashMap<>();
    // private static final Map<String, LocalDateTime> lastActiveTimeMap = new
    // ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    // 儲存所有連線的玩家（可依照房間編碼分群）
    // private static final ConcurrentHashMap<String, WebSocketSession> sessions =
    // new ConcurrentHashMap<>();

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

        // disconnectTimeMap.remove(token);
        // 玩家斷線後重新連線
        List<MatchModel> matchesAsP1 = matchRepository.findAllByPlayer1Id(token);
        List<MatchModel> matchesAsP2 = matchRepository.findAllByPlayer2Id(token);

        // lastActiveTimeMap.put(token, LocalDateTime.now());

        MatchModel match = Stream.concat(matchesAsP1.stream(), matchesAsP2.stream())
                .filter(m -> "playing".equals(m.getMatchStatus()))
                .findFirst()
                .orElse(null);
        if (match != null && "playing".equals(match.getMatchStatus())) {
            boolean yourTurn = match.getCurrentPlayerId().equals(token);
            String restoreMsg = String.format(
                    "{\"type\":\"restore\",\"matchId\":\"%s\",\"yourTurn\":%s,\"score1\":%d,\"score2\":%d}",
                    match.getId(), yourTurn, match.getScore1(), match.getScore2());
            sendToToken(token, restoreMsg);
            System.out.println("已恢復玩家對局狀態：" + token);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String token = sessionIdToTokenMap.remove(session.getId());
        if (token != null) {
            tokenSessionMap.remove(token);
            // disconnectTimeMap.put(token, LocalDateTime.now());
        }

        System.out.println("玩家離線: " + session.getId());
    }

    private String getQueryParam(WebSocketSession session, String key) {
        String query = session.getUri().getQuery();
        if (query == null)
            return null;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(key))
                return pair[1];
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
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        JsonNode msg = mapper.readTree(message.getPayload());
        String type = msg.get("type").asText();

        try {
            // if ("send".equals(type)) {
            //     // Extract shot message as-is
            //     String matchId = msg.get("matchId").asText();

            //     // Lookup both player tokens by matchId
            //     MatchModel match = matchRepository.findById(matchId).orElse(null);
            //     if (match == null)
            //         return;

            //     String player1Id = match.getPlayer1Id();
            //     String player2Id = match.getPlayer2Id();

            //     ((com.fasterxml.jackson.databind.node.ObjectNode) msg).put("type", "shot");
            //     String modifiedMessage = mapper.writeValueAsString(msg);

            //     // Send the message to both players (or only to the opponent)
            //     GameWebSocketHandler.sendToToken(player1Id, modifiedMessage);
            //     GameWebSocketHandler.sendToToken(player2Id, modifiedMessage);

            //     System.out.println("[Forwarded] shot event from " + sessionIdToTokenMap.get(session.getId()));
            // }

            if ("state_update".equals(type)) {
            String rawMessage = message.getPayload();
            String matchId = msg.get("matchId").asText();
            MatchModel match = matchRepository.findById(matchId).orElse(null);
            if (match == null)
                return;

            // 找出目前這個 session 的 token
            String senderToken = sessionIdToTokenMap.get(session.getId());
            // System.out.println("senderToken = " + senderToken);
            if (senderToken == null)
                return;

            String opponentToken;
            if (senderToken.equals(match.getPlayer1Id())) {
                // System.out.println("player2Id = " + match.getPlayer2Id());
                opponentToken = match.getPlayer2Id();
            } else if (senderToken.equals(match.getPlayer2Id())) {
                // System.out.println("player1Id = " + match.getPlayer1Id());
                opponentToken = match.getPlayer1Id();
            } else {
                System.out.println("找不到對手的 token");
                // 非這場對局的玩家，不處理
                return;
            }

            System.out.println(" rawMessage = " + rawMessage);

            sendToToken(opponentToken, rawMessage);
            System.out.println("[Forwarded] state_update from " + senderToken + " to opponent " + opponentToken);
        }

        //{ type: "turn_done", matchId: "xxx" }

        if ("turn_done".equals(type)) {
        String matchId = msg.get("matchId").asText();
        String senderToken = sessionIdToTokenMap.get(session.getId());
        if (senderToken == null) return;

        MatchModel match = matchRepository.findById(matchId).orElse(null);
        if (match == null) return;

        // 1. 檢查是否是當前玩家
        if (!senderToken.equals(match.getCurrentPlayerId())) {
            sendToToken(senderToken, "{\"type\":\"error\",\"message\":\"不是你的回合\"}");
            return;
        }

        // 2. 切換回合
        String nextPlayerId = senderToken.equals(match.getPlayer1Id())
            ? match.getPlayer2Id()
            : match.getPlayer1Id();

        match.setCurrentPlayerId(nextPlayerId);
        matchRepository.save(match);

        // 3. 廣播 turn_update 給雙方  // {"type": "turn_update","yourTurn": true}
        String msgToP1 = String.format("{\"type\":\"turn_update\",\"yourTurn\":%s}",
            match.getPlayer1Id().equals(nextPlayerId));
        String msgToP2 = String.format("{\"type\":\"turn_update\",\"yourTurn\":%s}",
            match.getPlayer2Id().equals(nextPlayerId));

        sendToToken(match.getPlayer1Id(), msgToP1);
        sendToToken(match.getPlayer2Id(), msgToP2);

        System.out.println("回合已切換到: " + nextPlayerId);
    }

    if ("goat".equals(type)) {
        String matchId = msg.get("matchId").asText();
        int score1 = msg.get("score1").asInt();
        int score2 = msg.get("score2").asInt();

        MatchModel match = matchRepository.findById(matchId).orElse(null);
        if (match != null) {
            match.setScore1(score1);
            match.setScore2(score2);
            matchRepository.save(match);

        String msgToGame = String.format("{\"type\":\"goatupdate\",\"score1\":%d,\"score2\":%d}", score1, score2);
        sendToToken(match.getPlayer1Id(), msgToGame);
        sendToToken(match.getPlayer2Id(), msgToGame);

        System.out.println("[Goat] 比分已更新並廣播");
    } else {
        System.out.println("[Goat] 找不到 matchId: " + matchId);
    }
}

    }catch (Exception e) {
            System.out.println("處理訊息時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"處理訊息失敗\"}"));
        }
    }

    // if ("shot".equals(type)) {
    // String matchId = json.get("matchId").asText();
    // MatchModel match = matchRepository.findById(matchId).orElse(null);
    // if (match == null) return;

    // String senderToken = getQueryParam(session, "token");
    // if (!match.getCurrentPlayerId().equals(senderToken)) {
    // sendToToken(senderToken, "{\"type\":\"error\",\"message\":\"不是你的回合\"}");
    // return;
    // }

    // JsonNode payload = json.get("payload");

    // // 判斷 chessId 是否是自己的棋子（暫略）
    // String chessId = payload.get("id").asText();

    // boolean isPlayer1 = senderToken.equals(match.getPlayer1Id());
    // boolean isValid = (isPlayer1 && chessId.startsWith("p1_")) || (!isPlayer1 &&
    // chessId.startsWith("p2_"));

    // // System.out.println("收到 chessId = " + chessId);
    // // System.out.println("isPlayer1 = " + isPlayer1);
    // // System.out.println("isValid = " + isValid);

    // if (!isValid) {
    // sendToToken(senderToken, "{\"type\":\"error\",\"message\":\"不能操作對手的棋子\"}");
    // return;
    // }

    // MoveCommand cmd;

    // // 加一個限制最大最小力
    // try {
    // cmd = mapper.readValue(payload.toString(), MoveCommand.class);
    // } catch (Exception ex) {
    // session.sendMessage(new
    // TextMessage("{\"type\":\"error\",\"message\":\"MoveCommand 解析失敗\"}"));
    // return;
    // }

    // cmd.setSessionId(matchId);

    // if (!physicsEngineService.hasSession(matchId)) {
    // List<EntityState> initStates = new GameService().initEntityStates();
    // physicsEngineService.createSession(matchId, initStates);
    // }

    // physicsEngineService.enqueue(cmd);

    // // 進球模擬（簡化處理）
    // if (isPlayer1) {
    // match.setScore1(match.getScore1() + 1);
    // } else {
    // match.setScore2(match.getScore2() + 1);
    // }

    // if (match.getScore1() >= 7 || match.getScore2() >= 7) {
    // match.setMatchStatus("finished");
    // match.setEndedAt(LocalDateTime.now());
    // match.setWinnerId(match.getScore1() >= 7 ? match.getPlayer1Id() :
    // match.getPlayer2Id());
    // matchRepository.save(match);

    // roomRepository.deleteById(match.getRoomId());

    // String msg = String.format("{\"type\":\"game_over\",\"winner\":\"%s\"}",
    // match.getWinnerId());
    // sendToToken(match.getPlayer1Id(), msg);
    // sendToToken(match.getPlayer2Id(), msg);
    // return;
    // }

    // // 等待物理結束後自動切換回合
    // match.setWaitingForTurnSwitch(true);
    // matchRepository.save(match);
    // }

    // 系統測試中 為求方便暫時關閉
    // @Scheduled(fixedRate = 1000)
    // public void autoLogout() {
    // LocalDateTime now = LocalDateTime.now();

    // for (Map.Entry<String, LocalDateTime> entry : disconnectTimeMap.entrySet()) {
    // String token = entry.getKey();
    // LocalDateTime disconnectTime = entry.getValue();

    // if (Duration.between(disconnectTime, now).getSeconds() >= 300) {
    // PlayerModel player = playerRepository.findByToken(token);
    // if (player != null) {
    // player.setToken(null);
    // playerRepository.save(player);
    // System.out.println("已清除資料庫中的 token：" + token);
    // }

    // tokenSessionMap.remove(token);
    // sessionIdToTokenMap.values().remove(token);
    // disconnectTimeMap.remove(token);
    // System.out.println("登出並移除記憶體記錄：" + token);
    // }
    // }
    // }

    // public void broadcast(String matchId, String update) throws Exception {
    // MatchModel match = matchRepository.findById(matchId).orElse(null);
    // if (match == null) {
    // System.out.println("找不到 matchId：" + matchId);
    // return;
    // }

    // String player1Token = match.getPlayer1Id();
    // String player2Token = match.getPlayer2Id();

    // WebSocketSession session1 = tokenSessionMap.get(player1Token);
    // WebSocketSession session2 = tokenSessionMap.get(player2Token);

    // if (session1 != null && session1.isOpen()) {
    // session1.sendMessage(new TextMessage(update));
    // }

    // if (session2 != null && session2.isOpen()) {
    // session2.sendMessage(new TextMessage(update));
    // }
    // }
}

