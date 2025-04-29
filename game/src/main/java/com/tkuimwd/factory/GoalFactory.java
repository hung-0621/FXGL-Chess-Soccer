package com.tkuimwd.factory;

import com.tkuimwd.component.GoalComponent;
import com.tkuimwd.model.GoalModel;
import com.tkuimwd.type.EntityType;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;

public class GoalFactory implements EntityFactory {
    @Spawns("Goal")
    public Entity spawnGoal(SpawnData data) {
        GoalModel goalModel = data.get("goalModel");
        // PhysicsComponent physics = setPhysics(goalModel);
        HitBox hitBox = new HitBox(BoundingShape.box(goalModel.getWidth(), goalModel.getHeight()));
        GoalComponent goalComponent = new GoalComponent();

        return FXGL.entityBuilder(data)
                .type(EntityType.GOAL)
                .bbox(hitBox)
                .with(goalComponent)
                .build();
    }

    // private PhysicsComponent setPhysics(GoalModel model) {
    //     PhysicsComponent physics = new PhysicsComponent();

    //     BodyDef bd = new BodyDef();
    //     bd.setType(BodyType.STATIC);
    //     physics.setBodyType(BodyType.STATIC);

    //     FixtureDef fd = new FixtureDef()
    //             .sensor(true);
    //     physics.setFixtureDef(fd);

    //     return physics;
    // }

}
