package com.tkuimwd.component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.Body;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.tkuimwd.api.dto.StateUpdate;
import com.tkuimwd.event.ChessReleaseEvent;
import com.tkuimwd.type.EntityType;
import com.tkuimwd.ui.ScoreBoard;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.util.Duration;

import com.tkuimwd.api.API;
import com.tkuimwd.api.dto.EntityState;
import com.tkuimwd.api.dto.MatchData;
import com.tkuimwd.api.dto.MoveCommand;
import com.tkuimwd.api.dto.ShotCommand;
import com.tkuimwd.Config;
import com.tkuimwd.Main;

public class NetworkComponent extends Component {

    private WebSocket ws;
    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, Entity> idMap = new HashMap<>();
    private int tick = 0;
    private boolean isMyTurn;

    private MatchData matchData = Config.matchData;
    private final String playerToken = Config.token;
    private final boolean isHost = Config.isHost;

    @Override
    public void onAdded() {

        // 1) 先 collect idMap
        idMap.clear();
        List<Entity> p1List = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.P1_CHESS);
        for (int i = 0; i < p1List.size(); i++) {
            Entity e = p1List.get(i);

            // 設定屬性
            ChessComponent chessComponent = e.getComponent(ChessComponent.class);
            String id = chessComponent.getId();
            idMap.put(id, e);

        }

        List<Entity> p2List = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.P2_CHESS);
        for (int i = 0; i < p2List.size(); i++) {
            Entity e = p2List.get(i);
            ChessComponent chessComponent = e.getComponent(ChessComponent.class);
            String id = chessComponent.getId();
            idMap.put(id, e);
        }

        Entity football = FXGL.getGameWorld().getEntitiesByType(EntityType.FOOTBALL).get(0);
        FootBallComponent footBallComponent = football.getComponent(FootBallComponent.class);
        String id = footBallComponent.getId();
        if (id != null) {
            idMap.put(id, football);
        }

        isMyTurn = matchData.getCurrentPlayerId().equals(playerToken);
        if (isMyTurn) {
            unlockChess();
        } else {
            lockChess();
        }

        if (playerToken == null || playerToken.isEmpty()) {
            System.err.println("[Network] token is null or empty");
            return;
        }
        System.out.println("[Network] token: " + playerToken);

        // 2) 建 WS，連到你的 relay server
        HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(
                        URI.create("ws://localhost:8080/ws/game?token=" + playerToken),
                        new WSListener())
                .whenComplete((ws, err) -> {
                    if (err != null) {
                        System.err.println("[Network] WS 連線失敗: " + err.getMessage());
                        err.printStackTrace();
                    } else {
                        this.ws = ws;
                        System.out.println("[Network] WS 已連線");
                        // createListener();
                    }
                });
    }

    // private void createListener() {
    // System.out.println("監聽事件: ChessReleaseEvent.CHESS_RELEASE");
    // FXGL.getEventBus().addEventHandler(
    // ChessReleaseEvent.CHESS_RELEASE,
    // e -> {
    // MoveCommand mc = new MoveCommand(
    // e.getId(),
    // e.getStartX(), e.getStartY(),
    // e.getEndX(), e.getEndY());

    // ShotCommand sc = new ShotCommand(tick++, "send", matchData.getId(), mc);
    // sendCommand(sc);
    // });
    // }

    // private void sendCommand(ShotCommand shotcommand) {
    // if (ws != null && !ws.isOutputClosed()) {
    // ObjectNode msg = mapper.createObjectNode()
    // .put("type", shotcommand.getType())
    // .put("matchId", shotcommand.getMatchId())
    // .set("payload", mapper.valueToTree(shotcommand.getCommand()));

    // System.out.println(msg.toString());
    // ws.sendText(msg.toString(), true);
    // }
    // }

    @Override
    public void onUpdate(double tpf) {
        StateUpdate update = collectState(tick);
        if (!update.getStates().isEmpty()) {
            tick++;
            sendStateUpdate(update);
        } else if (update.getStates().isEmpty() && tick > 0) {
            tick = 0;
            sendTurnDone();
        }
    }

    /** 把當前所有實體的位置打包 */
    private StateUpdate collectState(int tick) {
        List<EntityState> list = new ArrayList<>();
        list.clear();
        idMap.forEach((id, e) -> {
            PhysicsComponent phy = e.getComponent(PhysicsComponent.class);

            if (e != null) {
                double x = e.getX();
                double y = e.getY();
                double vx = phy.getLinearVelocity().getX();
                double vy = phy.getLinearVelocity().getY();
                if (vx == 0 && vy == 0) {
                    return;
                }
                list.add(new EntityState(id, x, y, vx, vy));
            }
        });
        StateUpdate update = new StateUpdate(tick, matchData.getId(), list);
        return update;
    }

    private void sendStateUpdate(StateUpdate update) {
        if (ws != null && ws.isOutputClosed() == false) {
            ObjectNode msg = mapper.createObjectNode();
            msg.put("type", update.getType());
            msg.put("matchId", update.getMatchId());
            msg.put("tick", update.getTick());
            msg.set("payload", mapper.valueToTree(update.getStates()));
            ws.sendText(msg.toString(), true);

            System.out.println("sendStateUpdate: " + msg.toString());
        }
    }

    private void sendTurnDone() {
        if (ws != null && ws.isOutputClosed() == false) {
            ObjectNode msg = mapper.createObjectNode();
            msg.put("type", "turn_done");
            msg.put("matchId", matchData.getId());
            ws.sendText(msg.toString(), true);

            System.out.println("=== TurnDone ===");
        }
    }

    private class WSListener implements WebSocket.Listener {
        private StringBuilder buf = new StringBuilder();

        @Override
        public CompletionStage<?> onText(WebSocket webSocket,
                CharSequence data,
                boolean last) {
            // 1) 將每次來的片段累積
            buf.append(data);

            if (!last) {
                // 2) 還沒到結尾，先要求下一個 frame
                webSocket.request(1);
                return CompletableFuture.completedFuture(null);
            }

            // 3) last==true，這時候 buf.toString() 才是完整 JSON
            String full = buf.toString();
            buf.setLength(0); // 清空，為下一條訊息做準備

            try {
                JsonNode root = mapper.readTree(full);
                if ("state_update".equals(root.get("type").asText())) {
                    List<EntityState> payload = Arrays.asList(
                            mapper.treeToValue(root.get("payload"),
                                    EntityState[].class));
                    FXGL.getGameTimer().runOnceAfter(() -> {
                        for (EntityState s : payload) {
                            applyRemote(s);
                        }
                    }, Duration.ZERO);
                } else if ("turn_update".equals(root.get("type").asText())) {
                    // 處理回合結束的邏輯
                    isMyTurn = root.get("yourTurn").asBoolean();
                    Config.isMyTurn = isMyTurn;
                    API.getMatchInfo(Config.matchId)
                            .thenAccept(matchInfo -> {
                                if (matchInfo != null && matchInfo.getMatchStatus().equals("playing")) {
                                    matchData = matchInfo;
                                }
                            })
                            .exceptionally(ex -> {
                                ex.printStackTrace();
                                return null;
                            });
                    Main.getScoreBoard().updateScoreBoard();

                    if (isMyTurn) {
                        unlockChess();
                    } else {
                        lockChess();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 4) 處理完畢，準備接收下一條 WebSocket 訊息
            webSocket.request(1);
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            // 一開始就先 request 一個 frame
            webSocket.request(1);
            WebSocket.Listener.super.onOpen(webSocket);
        }

    }

    private void applyRemote(EntityState payload) {
        FXGL.getGameTimer().runOnceAfter(() -> {
            Entity e = idMap.get(payload.getId());
            if (e == null) {
                System.err.println("找不到實體: " + payload.getId());
                return;
            }

            PhysicsComponent phy = e.getComponent(PhysicsComponent.class);
            phy.overwritePosition(new Point2D(payload.getX(), payload.getY()));
            // System.out.println("applyRemote: " + payload.getId() + " " + payload.getX() +
            // " " + payload.getY());
        }, Duration.ZERO);
    }

    // 用來處理進球後重置初始位置
    private void applyReset() {
        double[][] pi_chess_position = Config.player1_chess_position;
        double[][] p2_chess_position = Config.player2_chess_position;
        Point2D football_position = Config.FOOTBALL_POSITION;

        idMap.forEach((id, e) -> {
            if (id.startsWith("p1_chess")) {
                int index = Integer.parseInt(id.substring(9));
                e.setPosition(pi_chess_position[index][0], pi_chess_position[index][1]);
                e.getComponent(PhysicsComponent.class).setLinearVelocity(0, 0);
            } else if (id.startsWith("p2_chess")) {
                int index = Integer.parseInt(id.substring(9));
                e.setPosition(p2_chess_position[index][0], p2_chess_position[index][1]);
                e.getComponent(PhysicsComponent.class).setLinearVelocity(0, 0);
            } else if (id.equals("football")) {
                e.setPosition(football_position.getX(), football_position.getY());
                e.getComponent(PhysicsComponent.class).setLinearVelocity(0, 0);
            }
        });
    }

    private void lockChess() {
        idMap.forEach((id, e) -> {
            if (!e.hasComponent(ChessComponent.class)
                    || !e.hasComponent(AimComponent.class)) {
                return;
            }
            e.getComponent(ChessComponent.class).setLock();
            e.getComponent(AimComponent.class).setLock();
        });
    }

    private void unlockChess() {
        idMap.forEach((id, e) -> {
            if (!e.hasComponent(ChessComponent.class)
                    || !e.hasComponent(AimComponent.class))
                return;

            if (isHost && e.getType() == EntityType.P1_CHESS
                    || !isHost && e.getType() == EntityType.P2_CHESS) {
                e.getComponent(ChessComponent.class).setUnlock();
                e.getComponent(AimComponent.class).setUnlock();
            } else {
                e.getComponent(ChessComponent.class).setLock();
                e.getComponent(AimComponent.class).setLock();
            }
        });
    }
}
