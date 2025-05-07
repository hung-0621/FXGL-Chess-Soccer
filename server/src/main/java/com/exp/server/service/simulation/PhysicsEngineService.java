package com.exp.server.service.simulation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.exp.server.service.simulation.dto.EntityState;
import com.exp.server.service.simulation.dto.MoveCommand;
import com.exp.server.service.simulation.dto.StateUpdate;
import com.exp.server.websocket.GameWebSocketHandler;

@Service
public class PhysicsEngineService {
    private final GameWebSocketHandler wsHandler;
    private final PhysicsWorldWapper worldWrapper;

    /** sessionId → SimulationSession */
    private final Map<String, SimulationSession> sessions = new ConcurrentHashMap<>();

    /** 全局遞增的 Tick 序號 */
    private final AtomicInteger tick = new AtomicInteger();

    public PhysicsEngineService(GameWebSocketHandler wsHandler,
                                PhysicsWorldWapper worldWrapper) {
        this.wsHandler     = wsHandler;
        this.worldWrapper  = worldWrapper;
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

    /** 每 16ms 自動執行一次 world.step + broadcast */
    @Scheduled(fixedRate = 16)
    public void stepAll() {
        sessions.forEach((session_id, session) -> {
            int seq = tick.incrementAndGet();
            StateUpdate update = session.stepAndGetStates(seq);
            try {
                wsHandler.broadcast(session_id, update.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
