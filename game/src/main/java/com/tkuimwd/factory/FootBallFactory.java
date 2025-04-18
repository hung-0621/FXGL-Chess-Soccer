package com.tkuimwd.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;

import javafx.scene.shape.Circle;

public class FootBallFactory implements EntityFactory {
    @Spawns("FootBall")
    public Entity spawnSuccer(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(new Circle(12))
                .with(new IrremovableComponent())
                .collidable()
                .build();
    }
}
