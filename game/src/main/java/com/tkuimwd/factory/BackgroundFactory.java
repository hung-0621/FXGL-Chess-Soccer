package com.tkuimwd.factory;

import java.io.InputStream;

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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.tkuimwd.model.BackgroundModel;
import com.tkuimwd.type.EntityType;

public class BackgroundFactory implements EntityFactory {

    @Spawns("Background")
    public Entity spawnBackground(SpawnData data) {

        BackgroundModel backgroundModel = data.get("backgroundModel");
        ImageView background = setView(backgroundModel);
        PhysicsComponent physics = setPhysics();

        return FXGL.entityBuilder(data)
                .type(EntityType.BACKGROUND)
                .view(background)
                .with(physics, new IrremovableComponent()) // physics removed
                .zIndex(-100)
                .build();
    }

    public ImageView setView(BackgroundModel backgroundModel) {
        String backgroundImagePath = backgroundModel.get_background_image_path();
        InputStream input = getClass().getResourceAsStream(backgroundImagePath);
        if (input == null) {
            throw new IllegalStateException("找不到資源：" + backgroundImagePath);
        }
        Image backgroundImage = new Image(input);
        ImageView background = new ImageView(backgroundImage);
        background.preserveRatioProperty().set(true);
        return background;
    }

    private PhysicsComponent setPhysics() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef()
                .restitution(0f)
                .friction(1f)
                .density(0f));
        BodyDef bd = new BodyDef();
        bd.setType(BodyType.STATIC);
        physics.setBodyDef(bd);
        return physics;
    }
}
