package com.tkuimwd.ui.util;

import com.almasb.fxgl.dsl.FXGL;
import com.tkuimwd.Config;

import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UiManager {

    public static ImageView createBackground(String path) {
        ImageView background = SourceGetter.getImageView(path);
        background.setFitWidth(Config.WIDTH);
        background.setFitHeight(Config.HEIGHT);
        return background;
    }

    public static ImageView createBackground(String path, ColorAdjust adjust) {
        ImageView background = SourceGetter.getImageView(path);
        background.setFitWidth(Config.WIDTH);
        background.setFitHeight(Config.HEIGHT);
        background.setEffect(adjust);
        return background;
    }

    public static Text createTitle(double x, double y, String titleText) {
        Text title = FXGL.getUIFactoryService().newText(titleText, Color.WHITE, 40);
        title.setTranslateX(x);
        title.setTranslateY(y);
        return title;
    }

    public static Text createBackButton(double x, double y, Node root) {
        Text backButton = new Text("🔙");
        backButton.setTranslateX(x);
        backButton.setTranslateY(y);
        backButton.setFill(Color.WHITE);
        backButton.setFont(FXGL.getUIFactoryService().newFont(30));
        backButton.setOnMouseClicked(e -> {
            SourceGetter.runLeaveAnimation(root, () -> {
                FXGL.getSceneService().popSubScene(); // main
            });
        });
        return backButton;
    }
}
