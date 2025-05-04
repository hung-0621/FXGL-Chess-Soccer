package com.tkuimwd.util;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import com.almasb.fxgl.dsl.FXGL;

public class MouseTracker {
    public void tracker() {
        FXGL.getGameScene().getRoot().addEventHandler(
                MouseEvent.MOUSE_MOVED,
                e -> {
                    Point2D world = FXGL.getInput().getMousePositionWorld();
                    double worldX = world.getX();
                    double worldY = world.getY();

                    System.out.printf("world=(%.1f,%.1f)%n", worldX, worldY);
                });
    }
}
