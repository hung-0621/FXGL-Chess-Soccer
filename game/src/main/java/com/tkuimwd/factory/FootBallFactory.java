package com.tkuimwd.factory;

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

import javafx.scene.shape.Circle;

public class FootBallFactory implements EntityFactory {
    @Spawns("FootBall")
    public Entity spawnSuccer(SpawnData data) {
        
        Circle footBall = setView();
        PhysicsComponent physics = setPhysics();

        return FXGL.entityBuilder(data)
                .viewWithBBox(footBall)
                .with(new IrremovableComponent())
                .with(physics)
                .collidable()
                .build();
    }

    public Circle setView() {
        Circle footBall = new Circle(12);
        return footBall;
    }

    public PhysicsComponent setPhysics() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef()
            .restitution(0.5f)
            .friction(0.5f)
            .density(0.5f));
        BodyDef bd = new BodyDef();
        bd.setType(BodyType.DYNAMIC);
        bd.setFixedRotation(true);
        bd.setLinearDamping(0.3f);
        physics.setBodyDef(bd);
        return physics;
    }
}
