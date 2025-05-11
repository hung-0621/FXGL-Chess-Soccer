package com.tkuimwd.factory;

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
import com.tkuimwd.component.FootBallComponent;
import com.tkuimwd.model.FootBallModel;
import com.tkuimwd.type.EntityType;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class FootBallFactory implements EntityFactory {
    @Spawns("FootBall")
    public Entity spawnSuccer(SpawnData data) {

        FootBallModel model = data.get("footBallModel");
        FootBallComponent component = new FootBallComponent();
        double radius = model.getRadius();
        Circle footBall = setView();
        // PhysicsComponent physics = setPhysics();

        return FXGL.entityBuilder(data)
                .type(EntityType.FOOTBALL)
                .view(footBall)
                .bbox(new HitBox(new Point2D(-radius, -radius), BoundingShape.circle(radius)))
                .with(component, new IrremovableComponent()) // physics removed
                .collidable()
                .build();
    }

    public Circle setView() {
        Circle footBall = new Circle(12);
        footBall.setFill(Color.BLACK);
        footBall.setStroke(Color.WHITE);
        return footBall;
    }

    public PhysicsComponent setPhysics() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef()
                .restitution(0.9f)
                .friction(0.1f)
                .density(0.5f));
        BodyDef bd = new BodyDef();
        bd.setType(BodyType.DYNAMIC);
        bd.setFixedRotation(true);
        bd.setLinearDamping(0.2f);
        physics.setBodyDef(bd);
        return physics;
    }
}
