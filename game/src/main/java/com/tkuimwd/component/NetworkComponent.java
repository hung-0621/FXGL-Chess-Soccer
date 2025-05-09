package com.tkuimwd.component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.tkuimwd.api.dto.StateUpdate;

import javafx.util.Duration;

import com.tkuimwd.api.dto.EntityState;

public class NetworkComponent extends Component {

    private WebSocket ws;
    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, Entity> idMap = new HashMap<>();

    @Override
    public void onAdded() {
        // 1) 先 collect idMap
        FXGL.getGameWorld().getEntities()
                .forEach(e -> {
                    String id = e.getString("id");
                    if (id != null)
                        idMap.put(id, e);
                });

        // 2) 建 WS，連到你的 relay server
        HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create("ws://localhost:8080/ws/game?room=room123"),
                        new WSListener())
                .thenAccept(ws -> this.ws = ws);
    }

    @Override
    public void onUpdate(double tpf) {
        // 每幀或固定間隔，collect 全部實體狀態並 broadcast
        StateUpdate upd = collectState();
        System.out.println(upd.toString());
        sendStateUpdate(upd);
    }

    /** 把當前所有實體的位置＋速度打包 */
    private StateUpdate collectState() {
        List<EntityState> list = new ArrayList<>();
        int seq = 0;

        idMap.forEach((id, e) -> {
            PhysicsComponent phy = e.getComponent(PhysicsComponent.class);

            double x = e.getX();
            double y = e.getY();
            double vx = phy.getLinearVelocity().getX();
            double vy = phy.getLinearVelocity().getY();

            list.add(new EntityState(id, x, y, vx, vy));
        });
        StateUpdate update = new StateUpdate(seq, list);
        seq++;
        return update;
    }

    private void sendStateUpdate(StateUpdate upd) {
        if (ws != null && ws.isOutputClosed() == false) {
            ObjectNode msg = mapper.createObjectNode();
            msg.put("type", "stateUpdate");
            msg.set("payload", mapper.valueToTree(upd));
            ws.sendText(msg.toString(), true);
        }
    }

    private class WSListener implements WebSocket.Listener {
        @Override
        public CompletionStage<?> onText(WebSocket webSocket,
                CharSequence data, boolean last) {
            try {
                JsonNode root = mapper.readTree(data.toString());
                if ("stateUpdate".equals(root.get("type").asText())) {
                    StateUpdate upd = mapper.treeToValue(
                            root.get("payload"), StateUpdate.class);
                    // 收到別人的狀態 → 更新本地 (忽略自己)
                    FXGL.getGameTimer().runOnceAfter(() -> applyRemote(upd), Duration.ZERO);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }
    }

    /** 把遠端傳來的物件狀態覆寫到本地 Entity */
    private void applyRemote(StateUpdate update) {
        for (EntityState s : update.getStates()) {
            Entity e = idMap.get(s.getId());
            if (e == null)
                continue;
            // optionally skip if it's this client本地(input)主控的 piece
            e.setPosition(s.getX(), s.getY());
            e.getComponent(PhysicsComponent.class)
                    .setLinearVelocity(s.getVx(), s.getVy());
        }
    }
}
