package com.tkuimwd.factory;

import com.tkuimwd.type.Role;
import com.tkuimwd.model.ChessModel;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ChessFactory implements EntityFactory {

    @Spawns("Chess")
    public Entity spawnPlayer1(SpawnData data) {
        ChessModel chessModel = data.get("chessModel");
        Circle chess = setView(chessModel);
        PhysicsComponent physics = setPhysics();

        return FXGL.entityBuilder(data)
                .view(chess)
                .with(new IrremovableComponent())
                .with(physics)
                .collidable()
                .build();
    }

    public Circle setView(ChessModel chessModel) {
        Circle chess = new Circle(chessModel.getSize());
        chess.setFill(chessModel.getRole() == Role.PLAYER1 ? Color.BLUE : Color.RED);
        chess.setStroke(Color.WHITE);
        chess.setStrokeWidth(3);
        return chess;
    }

    public PhysicsComponent setPhysics(){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef()
            .restitution(0.5f)
            .friction(0.5f)
            .density(0.1f));
        BodyDef bd = new BodyDef();
        bd.setType(BodyType.DYNAMIC);
        physics.setBodyDef(bd);
        return physics;
    }
}
