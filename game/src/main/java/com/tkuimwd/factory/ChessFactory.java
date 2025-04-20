package com.tkuimwd.factory;

import com.tkuimwd.type.EntityType;
import com.tkuimwd.type.Role;
import com.tkuimwd.component.ChessComponent;
import com.tkuimwd.model.ChessModel;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ChessFactory implements EntityFactory {

    @Spawns("Chess")
    public Entity spawnPlayer1(SpawnData data) {
        ChessModel model = data.get("chessModel");
        double size = model.getSize();
        Circle view = setView(model);
        PhysicsComponent physics = setPhysics();
        ChessComponent chessComponent = new ChessComponent();

        return FXGL.entityBuilder(data)
                .type(EntityType.CHESS)
                .view(view)
                .bbox(new HitBox(new Point2D(-size, -size), BoundingShape.circle(size)))
                .with(physics, chessComponent, new IrremovableComponent())
                .collidable()
                .build();
    }

    private Circle setView(ChessModel chessModel) {
        Circle chess = new Circle(chessModel.getSize());
        chess.setFill(chessModel.getRole() == Role.PLAYER1 ? Color.BLUE : Color.RED);
        chess.setStroke(Color.WHITE);
        chess.setStrokeWidth(3);
        return chess;
    }

    private PhysicsComponent setPhysics() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef()
                .restitution(0.5f)
                .friction(0.5f)
                .density(1f));
        BodyDef bd = new BodyDef();
        bd.setType(BodyType.DYNAMIC);
        bd.setFixedRotation(true);
        bd.setLinearDamping(0.3f);
        physics.setBodyDef(bd);
        return physics;
    }
}
