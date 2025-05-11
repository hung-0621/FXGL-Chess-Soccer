package com.exp.server.service.simulation;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.exp.server.model.MatchModel;
import com.exp.server.repository.MatchRepository;
import com.exp.server.service.simulation.dto.EntityState;
import com.exp.server.service.simulation.dto.MoveCommand;
import com.exp.server.service.simulation.dto.StateUpdate;
import com.exp.server.websocket.GameWebSocketHandler;
import com.exp.server.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PhysicsEngineService {
    private final MatchRepository matchRepository;
    private final GameWebSocketHandler wsHandler;
    private final PhysicsWorldWapper worldWrapper;
    private final RoomRepository roomRepository;

    /** sessionId → SimulationSession */
    private final Map<String, SimulationSession> sessions = new ConcurrentHashMap<>();

    /** 全局遞增的 Tick 序號 */
    private final AtomicInteger tick = new AtomicInteger();

    public PhysicsEngineService(MatchRepository matchRepository,
            @Lazy GameWebSocketHandler wsHandler,
            PhysicsWorldWapper worldWrapper,
            RoomRepository roomRepository) {
        this.matchRepository = matchRepository;
        this.wsHandler = wsHandler;
        this.worldWrapper = worldWrapper;
        this.roomRepository = roomRepository;
    }

    /** 建立新對局，並初始化物件 */
    public void createSession(String sessionId, List<EntityState> dtos) {
        SimulationSession session = new SimulationSession(worldWrapper);
        session.init(dtos);
        sessions.put(sessionId, session);
    }

    /** 前端的 MoveCommand 轉到對應 Session */
    public void enqueue(MoveCommand cmd) {
        SimulationSession session = sessions.get(cmd.getSessionId());
        if (session != null) {
            session.enqueue(cmd);
        }
    }

    @Scheduled(fixedRate = 16)
    public void stepAll() {
        sessions.forEach((sessionId, session) -> {
            int seq = tick.incrementAndGet();
            StateUpdate update = session.stepAndGetStates(seq);

            if (update == null) {
                MatchModel match = matchRepository.findById(sessionId).orElse(null);
                if (match != null && match.isWaitingForTurnSwitch()) {
                    match.setWaitingForTurnSwitch(false);
                    match.setHasMovedThisTurn(false); // ✅ 切回合時清除操作記錄

                    // 切換回合
                    String nextTurn = match.getCurrentPlayerId().equals(match.getPlayer1Id())
                            ? match.getPlayer2Id()
                            : match.getPlayer1Id();

                    match.setCurrentPlayerId(nextTurn);
                    matchRepository.save(match);

                    String msgToP1 = String.format("{\"type\":\"turn_update\",\"yourTurn\":%s}",
                            match.getPlayer1Id().equals(nextTurn));
                    String msgToP2 = String.format("{\"type\":\"turn_update\",\"yourTurn\":%s}",
                            match.getPlayer2Id().equals(nextTurn));

                    GameWebSocketHandler.sendToToken(match.getPlayer1Id(), msgToP1);
                    GameWebSocketHandler.sendToToken(match.getPlayer2Id(), msgToP2);

                    System.out.println("自動切換回合：現在輪到 " + nextTurn);
                }
                return;
            }

            try {
                if (seq % 3 != 0)
                    return;

                String json = new ObjectMapper().writeValueAsString(update);
                wsHandler.broadcast(sessionId, json);

                // 檢查進球
                Optional<EntityState> maybeBall = update.getStates().stream()
                        .filter(e -> "football".equals(e.getId()))
                        .findFirst();

                if (maybeBall.isPresent()) {
                    EntityState ball = maybeBall.get();
                    if (isGoal(ball, true)) {
                        addScore(sessionId, false); // p2 射入 p1 球門
                    } else if (isGoal(ball, false)) {
                        addScore(sessionId, true); // p1 射入 p2 球門
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // 判斷球是否進入球門範圍
    private boolean isGoal(EntityState ball, boolean isP1Goal) {
        double bx = ball.getX();
        double by = ball.getY();
        double gx = isP1Goal ? 29 : 958;
        double gy = 324;
        double gw = 32;
        double gh = 179;

        return bx >= gx && bx <= gx + gw &&
                by >= gy && by <= gy + gh;
    }

    // 進球後加分與判斷勝利者
    private void addScore(String matchId, boolean isPlayer1) {
        MatchModel match = matchRepository.findById(matchId).orElse(null);
        if (match == null)
            return;

        if (isPlayer1) {
            match.setScore1(match.getScore1() + 1);
        } else {
            match.setScore2(match.getScore2() + 1);
        }

        if (match.getScore1() >= 7 || match.getScore2() >= 7) {
            match.setMatchStatus("finished");
            match.setEndedAt(LocalDateTime.now());
            match.setWinnerId(match.getScore1() >= 7 ? match.getPlayer1Id() : match.getPlayer2Id());
            matchRepository.save(match);
            roomRepository.deleteById(match.getRoomId());

            String msg = String.format("{\"type\":\"game_over\",\"winner\":\"%s\"}", match.getWinnerId());
            GameWebSocketHandler.sendToToken(match.getPlayer1Id(), msg);
            GameWebSocketHandler.sendToToken(match.getPlayer2Id(), msg);
            return;
        }

        String scoreMsg = String.format(
                "{\"type\":\"score_update\",\"score1\":%d,\"score2\":%d}",
                match.getScore1(), match.getScore2());
        GameWebSocketHandler.sendToToken(match.getPlayer1Id(), scoreMsg);
        GameWebSocketHandler.sendToToken(match.getPlayer2Id(), scoreMsg);

        matchRepository.save(match);
        resetEntities(matchId);
    }

    private void resetEntities(String sessionId) {
        SimulationSession session = sessions.get(sessionId);
        if (session == null)
            return;

        //來建立初始狀態
        List<EntityState> initStates = new GameService().initEntityStates();
        session.init(initStates); //把內部的 world 清空並重新加入所有實體

        // 將狀態同步給前端
        StateUpdate update = new StateUpdate(tick.incrementAndGet(), initStates);
        try {
            String json = new ObjectMapper().writeValueAsString(update);
            wsHandler.broadcast(sessionId, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasSession(String sessionId) {
        return sessions.containsKey(sessionId);
    }
}
