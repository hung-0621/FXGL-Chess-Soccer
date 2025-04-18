package com.tkuimwd.factory;

import java.io.InputStream;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.tkuimwd.model.BackgroundModel;

public class BackgroundFactory implements EntityFactory {

    @Spawns("Background")
    public Entity spawnBackground(SpawnData data) {

        BackgroundModel backgroundModel = data.get("backgroundModel");
        ImageView background = setView(backgroundModel);

        return FXGL.entityBuilder(data)
                .view(background)
                .with(new IrremovableComponent())
                .zIndex(-100)
                .build();
    }

    public ImageView setView(BackgroundModel backgroundModel){
        String backgroundImagePath = backgroundModel.get_background_image_path();
        InputStream input = getClass().getResourceAsStream(backgroundImagePath);
        if(input == null){
            throw new IllegalStateException("找不到資源：" + backgroundImagePath);
        }
        Image backgroundImage = new Image(input);
        ImageView background = new ImageView(backgroundImage);
        background.preserveRatioProperty().set(true);
        return background;
    }
}
