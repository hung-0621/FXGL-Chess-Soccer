package com.exp.server.service.simulation;

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
import com.exp.server.websocket.GameWebSocketHandler_00;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PhysicsEngineService {
    private final MatchRepository matchRepository;
    private final GameWebSocketHandler_00 wsHandler;
    private final PhysicsWorldWapper worldWrapper;

    /** sessionId → SimulationSession */
    private final Map<String, SimulationSession> sessions = new ConcurrentHashMap<>();

    /** 全局遞增的 Tick 序號 */
    private final AtomicInteger tick = new AtomicInteger();

    public PhysicsEngineService(MatchRepository matchRepository,
            @Lazy GameWebSocketHandler_00 wsHandler,
            PhysicsWorldWapper worldWrapper) {
        this.matchRepository = matchRepository;
        this.wsHandler = wsHandler;
        this.worldWrapper = worldWrapper;
    }

    /** 建立新對局，並初始化物件 */
    public void createSession(String sessionId, List<EntityState> dtos) {
        SimulationSession session = new SimulationSession(worldWrapper);
        session.init(dtos);
        sessions.put(sessionId, session);
    }

    // /** 前端的 MoveCommand 轉到對應 Session */
    // public void enqueue(MoveCommand cmd) {
    //     SimulationSession session = sessions.get(cmd.getSessionId());
    //     if (session != null) {
    //         session.enqueue(cmd);
    //     }
    // }

    /** 每 16ms 自動執行一次 world.step + broadcast */
    @Scheduled(fixedRate = 16)
    public void stepAll() {
        sessions.forEach((sessionId, session) -> {
            int seq = tick.incrementAndGet();
            StateUpdate update = session.stepAndGetStates(seq);

            if (update == null) {
                // 表示已經停止了：檢查是否要切換回合
                MatchModel match = matchRepository.findById(sessionId).orElse(null);
                if (match != null && match.isWaitingForTurnSwitch()) {
                    match.setWaitingForTurnSwitch(false);

                    // 切換回合
                    String nextTurn = match.getCurrentPlayerId().equals(match.getPlayer1Id())
                            ? match.getPlayer2Id()
                            : match.getPlayer1Id();

                    match.setCurrentPlayerId(nextTurn);
                    matchRepository.save(match);

                    // 廣播 turn 更新
                    String msgToP1 = String.format("{\"type\":\"turn_update\",\"yourTurn\":%s}",
                            match.getPlayer1Id().equals(nextTurn));
                    String msgToP2 = String.format("{\"type\":\"turn_update\",\"yourTurn\":%s}",
                            match.getPlayer2Id().equals(nextTurn));

                    GameWebSocketHandler_00.sendToToken(match.getPlayer1Id(), msgToP1);
                    GameWebSocketHandler_00.sendToToken(match.getPlayer2Id(), msgToP2);

                    System.out.println("自動切換回合：現在輪到 " + nextTurn);
                }

                return;
            }

            try {
                //每 3 tick 才回傳資料給前端
                if (seq % 3 != 0)
                    return;

                String json = new ObjectMapper().writeValueAsString(update);
                wsHandler.broadcast(sessionId, json);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public boolean hasSession(String sessionId) {
        return sessions.containsKey(sessionId);
    }
}
