package com.tkuimwd.controller;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;

import com.tkuimwd.factory.BackgroundFactory;
import com.tkuimwd.factory.ChessFactory;
import com.tkuimwd.factory.FootBallFactory;
import com.tkuimwd.factory.PlayerFactory;
import com.tkuimwd.model.BackgroundModel;
import com.tkuimwd.model.FootBallModel;
import com.tkuimwd.model.ChessModel;
import com.tkuimwd.model.PlayerModel;
import com.tkuimwd.type.Role;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class Main extends GameApplication {

    private static final int HEIGHT = 678;
    private static final int WIDTH = 1024;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("TKUIMWD");
        settings.setVersion("v1.0");
    }

    @Override
    protected void initGame() {
        // initMouseTracker();
        FXGL.getGameWorld().addEntityFactory(new BackgroundFactory());
        FXGL.getGameWorld().addEntityFactory(new PlayerFactory());
        FXGL.getGameWorld().addEntityFactory(new FootBallFactory());
        FXGL.getGameWorld().addEntityFactory(new ChessFactory());

        FXGL.spawn("Background", new SpawnData(0, 0).put("backgroundModel", new BackgroundModel()));
        FXGL.spawn("FootBall", new SpawnData(WIDTH / 2 + 1, HEIGHT / 2 + 7).put("footBallModel", new FootBallModel()));
        FXGL.spawn("Chess", new SpawnData(160, HEIGHT / 2 + 3).put("chessModel", new ChessModel(Role.PLAYER1)));
        FXGL.spawn("Chess", new SpawnData(850, HEIGHT / 2 + 3).put("chessModel", new ChessModel(Role.PLAYER2)));

        
    }

    @Override
    protected void initInput() {

    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 0);
    }

    // private void initMouseTracker() {
    //     // 把根節點的滑鼠移動事件綁定起來
    //     FXGL.getGameScene().getRoot().addEventHandler(
    //             MouseEvent.MOUSE_MOVED,
    //             e -> {
    //                 // 場景座標
    //                 double sceneX = e.getX();
    //                 double sceneY = e.getY();

    //                 // 世界座標（如果你有攝影機 / viewport）
    //                 Point2D world = FXGL.getInput().getMousePositionWorld();
    //                 double worldX = world.getX();
    //                 double worldY = world.getY();

    //                 System.out.printf("scene=(%.1f,%.1f)  world=(%.1f,%.1f)%n",
    //                         sceneX, sceneY, worldX, worldY);
    //             });
    // }

    public static void main(String[] args) {
        launch(args);
    }
}