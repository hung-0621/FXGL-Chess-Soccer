package com.exp.server.service.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import com.exp.server.service.simulation.dto.EntityState;
import com.exp.server.service.simulation.dto.MoveCommand;
import com.exp.server.service.simulation.dto.StateUpdate;
import com.exp.server.service.simulation.factory.BackgroundFactory;
import com.exp.server.service.simulation.factory.ChessFactory;
import com.exp.server.service.simulation.factory.FootBallFactory;
import com.exp.server.service.simulation.factory.GoalFactory;
import com.exp.server.service.simulation.factory.WallFactory;
import com.exp.server.util.UnitConverter;

public class SimulationSession {
    private final PhysicsWorldWapper worldWapper;
    private Map<String, Body> bodies;
    private Queue<MoveCommand> moveCommands;

    public SimulationSession(PhysicsWorldWapper worldWapper) {
        this.worldWapper = worldWapper;
        this.bodies = new ConcurrentHashMap<>();
        this.moveCommands = new ConcurrentLinkedQueue<>();
    }

    public void init(List<EntityState> dtos) {
        for (EntityState d : dtos) {
            Body b;
            float x = UnitConverter.pxToMeter((float) d.getX()); // meters
            float y = UnitConverter.pxToMeter((float) d.getY()); // meters
            switch (d.getType()) {
                case BACKGROUND:
                    BackgroundFactory backgroundFactory = new BackgroundFactory(worldWapper.getWorld());
                    b = backgroundFactory.createBody(x, y);
                    bodies.put(d.getId(), b);
                    break;
                case WALL:
                    WallFactory wallFactory = new WallFactory(worldWapper.getWorld());
                    b = wallFactory.createBody(x, y);
                    bodies.put(d.getId(), b);
                    break;
                case FOOTBALL:
                    FootBallFactory footBallFactory = new FootBallFactory(worldWapper.getWorld());
                    b = footBallFactory.createBody(x, y);
                    bodies.put(d.getId(), b);
                    break;
                case GOAL:
                    GoalFactory goalFactory = new GoalFactory(worldWapper.getWorld());
                    b = goalFactory.createBody(x, y);
                    bodies.put(d.getId(), b);
                    break;
                case P1_CHESS:
                    ChessFactory p1ChessFactory = new ChessFactory(worldWapper.getWorld());
                    b = p1ChessFactory.createBody(x, y);
                    bodies.put(d.getId(), b);
                    break;
                case P2_CHESS:
                    break;
                default:
                    break;
            }
        }
    }

    public void enqueue(MoveCommand command) {
        moveCommands.add(command);
    }

    public StateUpdate stepAndGetStates(int seq) {
        step();
        List<EntityState> list = new ArrayList<>();
        for (var e : bodies.entrySet()) {
            String id = e.getKey();
            Body b = e.getValue();
            Vec2 p = b.getPosition(); // (m)
            Vec2 v = b.getLinearVelocity(); // (m/s)

            // m → px
            float screenX = UnitConverter.meterToPx(p.x);
            float screenY = UnitConverter.worldMeterToScreenY(p.y);

            float velX = UnitConverter.meterToPx(v.x);
            float velY = -UnitConverter.meterToPx(v.y);
            // 注意：velocity Y 也要翻轉方向

            EntityState s = new EntityState(id, screenX, screenY, velX, velY);
            list.add(s);
        }
        StateUpdate update = new StateUpdate(seq, list);
        seq++;
        return update;
    }

    private void step() {

        while (!moveCommands.isEmpty()) {
            MoveCommand command = moveCommands.poll();
            Body body = bodies.get(command.getId());
            float startX = UnitConverter.pxToMeter((float) command.getStartX()); // meters
            float startY = UnitConverter.pxToMeter((float) command.getStartY()); // meters
            float endX = UnitConverter.pxToMeter((float) command.getEndX()); // meters
            float endY = UnitConverter.pxToMeter((float) command.getEndY()); // meters
            Vec2 start = new Vec2(startX, startY);
            Vec2 end = new Vec2(endX, endY);
            Vec2 v = new Vec2(start.sub(end));
            body.setLinearVelocity(v);
        }

        // 物理模擬：60Hz，8 velocity iterations, 3 position iterations
        worldWapper.getWorld().step(1 / 60f, 8, 3);
    }
}
