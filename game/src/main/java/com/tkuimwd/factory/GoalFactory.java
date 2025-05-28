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

public class GoalFactory implements EntityFactory {
    @Spawns("Goal")
    public Entity spawnGoal(SpawnData data) {
        GoalModel goalModel = data.get("goalModel");
        HitBox hitBox = new HitBox(BoundingShape.box(goalModel.getWidth(), goalModel.getHeight()));
        GoalComponent goalComponent = new GoalComponent(goalModel.getId());

        return FXGL.entityBuilder(data)
                .type(EntityType.GOAL)
                .bbox(hitBox)
                .with(goalComponent)
                .build();
    }
}
