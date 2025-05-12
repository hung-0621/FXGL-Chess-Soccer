package com.tkuimwd.ui;

import java.io.InputStream;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Util {
    public static void runLeaveAnimation(Node root, Runnable animationEndCallback) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(evt -> {
            if (animationEndCallback != null)
                animationEndCallback.run();
        });
        fadeOut.play();
    }

    public static ImageView getImageView(Node root,String path) {
        InputStream input = root.getClass().getResourceAsStream(path);
        if (input == null) {
            throw new IllegalStateException("找不到資源：" + path);
        }
        Image image = new Image(input);
        ImageView view = new ImageView(image);
        return view;
    }
}
