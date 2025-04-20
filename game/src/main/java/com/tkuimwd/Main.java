package com.tkuimwd;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;

import com.tkuimwd.factory.BackgroundFactory;
import com.tkuimwd.factory.ChessFactory;
import com.tkuimwd.factory.FootBallFactory;
import com.tkuimwd.factory.PlayerFactory;
import com.tkuimwd.factory.WallFactory;
import com.tkuimwd.model.BackgroundModel;
import com.tkuimwd.model.FootBallModel;
import com.tkuimwd.model.WallModel;
import com.tkuimwd.model.ChessModel;
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
        settings.setDeveloperMenuEnabled(true);
    }

    @Override
    protected void initGame() {
        // initMouseTracker();
        
        double[][] edges = {
                { 61, 54 }, { 61, 254 }, { 29, 254 }, { 29, 433 },
                { 61, 433 }, { 61, 651 }, { 958, 651 }, { 958, 433 },
                { 991, 433 }, { 991, 254 }, { 958, 254 }, { 958, 54 },
        };

        String backgroundImagePath = "/field.jpg";

        // Factory
        FXGL.getGameWorld().addEntityFactory(new BackgroundFactory());
        FXGL.getGameWorld().addEntityFactory(new WallFactory());
        FXGL.getGameWorld().addEntityFactory(new PlayerFactory());
        FXGL.getGameWorld().addEntityFactory(new FootBallFactory());
        FXGL.getGameWorld().addEntityFactory(new ChessFactory());

        // Model
        BackgroundModel backgroundModel = new BackgroundModel(backgroundImagePath);
        WallModel wallModel = new WallModel(edges);
        FootBallModel footBallModel = new FootBallModel(WIDTH / 2 - 1, HEIGHT / 2 + 13);
        ChessModel chessModel1 = new ChessModel(160, HEIGHT / 2 + 7, Role.PLAYER1);
        ChessModel chessModel2 = new ChessModel(850, HEIGHT / 2 + 7, Role.PLAYER2);

        // Spawn
        FXGL.spawn("Background", new SpawnData(0, 0).put("backgroundModel", backgroundModel));
        FXGL.spawn("Wall", new SpawnData(0, 0).put("wallModel", wallModel));
        FXGL.spawn("FootBall",
                new SpawnData(footBallModel.getX(), footBallModel.getY()).put("footBallModel", footBallModel));
        FXGL.spawn("Chess", new SpawnData(chessModel1.getX(), chessModel1.getY()).put("chessModel", chessModel1));
        FXGL.spawn("Chess", new SpawnData(chessModel2.getX(), chessModel2.getY()).put("chessModel", chessModel2));

    }

    @Override
    protected void initInput() {

    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 0);
    }

    private void initMouseTracker() {
        // 把根節點的滑鼠移動事件綁定起來
        FXGL.getGameScene().getRoot().addEventHandler(
                MouseEvent.MOUSE_MOVED,
                e -> {
                    // 場景座標
                    double sceneX = e.getX();
                    double sceneY = e.getY();

                    // 世界座標（如果你有攝影機 / viewport）
                    Point2D world = FXGL.getInput().getMousePositionWorld();
                    double worldX = world.getX();
                    double worldY = world.getY();

                    System.out.printf("scene=(%.1f,%.1f) world=(%.1f,%.1f)%n",
                            sceneX, sceneY, worldX, worldY);
                });
    }

    public static void main(String[] args) {
        launch(args);
    }
}