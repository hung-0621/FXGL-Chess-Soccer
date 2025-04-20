package com.tkuimwd.factory;

import java.util.ArrayList;

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
import com.tkuimwd.model.WallModel;
import com.tkuimwd.type.EntityType;

import javafx.geometry.Point2D;

public class WallFactory implements EntityFactory {

    @Spawns("Wall")
    public Entity spawnWall(SpawnData data) {
        WallModel wallModel = data.get("wallModel");
        ArrayList<Point2D> points = wallModel.getEdges();
        PhysicsComponent physics = setPhysics(points);

        return FXGL.entityBuilder(data)
                .type(EntityType.WALL)
                .bbox(new HitBox(BoundingShape.chain(points.toArray(new Point2D[0]))))
                .with(physics)
                .collidable()
                .build();
    }

    private PhysicsComponent setPhysics(ArrayList<Point2D> points) {
        PhysicsComponent physics = new PhysicsComponent();
        BodyDef bd = new BodyDef();
        bd.setType(BodyType.STATIC);
        physics.setBodyDef(bd);

        FixtureDef fd = new FixtureDef()
                .friction(1.0f)
                .restitution(0f)
                .density(0f);
        physics.setFixtureDef(fd);
        return physics;
    }
}
