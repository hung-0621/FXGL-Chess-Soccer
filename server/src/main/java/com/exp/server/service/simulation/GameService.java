package com.exp.server.service.simulation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exp.server.EntityConfig;
import com.exp.server.service.simulation.dto.EntityState;
import com.exp.server.service.simulation.dto.type.EntityType;

// set up EntityStatus
@Service
public class GameService {
    private List<EntityState> entityStatesList;

    public GameService() {
        entityStatesList = new ArrayList<>();
    }

    public List<EntityState> getEntityStatesList() {
        return entityStatesList;
    }

    //! Restful api /api/game/init 從這裡抓
    public List<EntityState> initEntityStates() {
        entityStatesList.clear();
        entityStatesList.add(getBackGroundEntityState("background"));
        entityStatesList.add(getWallEntityState("wall"));
        entityStatesList.add(getFootBallEntityState("football"));
        entityStatesList.add(getGoalEntityState("p1_goal"));
        entityStatesList.add(getGoalEntityState("p2_goal"));
        for (int i = 0; i < EntityConfig.P1_CHESS_POSITION.length; i++) {
            entityStatesList.add(getP1ChessEntityState("p1_chess_" + i));
        }
        for (int i = 0; i < EntityConfig.P2_CHESS_POSITION.length; i++) {
            entityStatesList.add(getP2ChessEntityState("p2_chess_" + i));
        }
        return entityStatesList;
    }

    private EntityState getBackGroundEntityState(String id) {
        return new EntityState(
                id,
                EntityType.BACKGROUND,
                EntityConfig.BACKGROUND_POSITION[0],
                EntityConfig.BACKGROUND_POSITION[1],
                0,
                0);
    }

    private EntityState getWallEntityState(String id) {
        return new EntityState(
                id,
                EntityType.WALL,
                EntityConfig.WALL_POSITION[0],
                EntityConfig.WALL_POSITION[1],
                0,
                0);
    }

    private EntityState getFootBallEntityState(String id) {
        return new EntityState(
                id,
                EntityType.FOOTBALL,
                EntityConfig.FOOTBALL_POSITION[0],
                EntityConfig.FOOTBALL_POSITION[1],
                0,
                0);
    }

    private EntityState getGoalEntityState(String id) {
        int index = Integer.parseInt(id.split("_")[2]);
        return new EntityState(
                id,
                EntityType.GOAL,
                EntityConfig.GOAL_POSITION[index][0],
                EntityConfig.GOAL_POSITION[index][1],
                0,
                0);
    }

    private EntityState getP1ChessEntityState(String id) {
        int index = Integer.parseInt(id.split("_")[2]);
        return new EntityState(
                id,
                EntityType.P1_CHESS,
                EntityConfig.P1_CHESS_POSITION[index][0],
                EntityConfig.P1_CHESS_POSITION[index][1],
                0,
                0);

    }

    private EntityState getP2ChessEntityState(String id) {
        int index = Integer.parseInt(id.split("_")[2]);
        return new EntityState(
                id,
                EntityType.P2_CHESS,
                EntityConfig.P2_CHESS_POSITION[index][0],
                EntityConfig.P2_CHESS_POSITION[index][1],
                0,
                0);
    }

}
