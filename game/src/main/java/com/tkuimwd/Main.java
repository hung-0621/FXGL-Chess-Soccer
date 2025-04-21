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
import com.tkuimwd.model.ScoreBoard;
import com.tkuimwd.type.Role;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;;

public class Main extends GameApplication {

    private static final int HEIGHT = Config.HEIGHT;
    private static final int WIDTH = Config.WIDTH;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("TKUIMWD");
        settings.setVersion("v1.0");
        settings.setDeveloperMenuEnabled(true);
        settings.setMainMenuEnabled(true);
    }

    @Override
    protected void initUI() {
        // score board
        ScoreBoard scoreBoard = new ScoreBoard();
        Label p1_score = new Label("P1: " + scoreBoard.getScore(Role.PLAYER1));
        Label p2_score = new Label("P2: " + scoreBoard.getScore(Role.PLAYER2));
        Label vs = new Label(" VS ");
        // main
        HBox pane = new HBox();
        pane.setSpacing(10);
        pane.setMinSize(WIDTH, 70);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-font-size: 20;");
        pane.getChildren().addAll(p1_score, vs, p2_score);

        FXGL.addUINode(pane, 0, 0);
    }

    @Override
    protected void initGame() {
        initMouseTracker();

        // config
        final double[][] WALL_EDGES = Config.WALL_EDGES;
        final String IMAGE_PATH = Config.IMAGE_PATH;
        final Point2D BACKGROUND_POSITION = Config.BACKGROUND_POSITION;
        final Point2D WALL_POSITION = Config.WALL_POSITION;
        final Point2D FOOTBALL_POSITION = Config.FOOTBALL_POSITION;
        final Point2D[] P1_CHESS_POSITION = Config.P1_CHESS_POSITION;
        final Point2D[] P2_CHESS_POSITION = Config.P2_CHESS_POSITION;

        // Factorys
        FXGL.getGameWorld().addEntityFactory(new BackgroundFactory());
        FXGL.getGameWorld().addEntityFactory(new WallFactory());
        FXGL.getGameWorld().addEntityFactory(new PlayerFactory());
        FXGL.getGameWorld().addEntityFactory(new FootBallFactory());
        FXGL.getGameWorld().addEntityFactory(new ChessFactory());

        // Model
        BackgroundModel backgroundModel = new BackgroundModel(IMAGE_PATH);
        WallModel wallModel = new WallModel(WALL_EDGES);
        FootBallModel footBallModel = new FootBallModel(FOOTBALL_POSITION);
        ChessModel[] p1_chess_model_list = new ChessModel[P1_CHESS_POSITION.length];
        ChessModel[] p2_chess_model_list = new ChessModel[P2_CHESS_POSITION.length];

        for (int i = 0; i < P1_CHESS_POSITION.length; i++) {
            p1_chess_model_list[i] = new ChessModel(P1_CHESS_POSITION[i], Role.PLAYER1);
        }

        for (int i = 0; i < P2_CHESS_POSITION.length; i++) {
            p2_chess_model_list[i] = new ChessModel(P2_CHESS_POSITION[i], Role.PLAYER2);
        }

        // Spawn
        FXGL.spawn("Background", new SpawnData(BACKGROUND_POSITION).put("backgroundModel", backgroundModel));
        FXGL.spawn("Wall", new SpawnData(WALL_POSITION).put("wallModel", wallModel));
        FXGL.spawn("FootBall",
                new SpawnData(footBallModel.getX(), footBallModel.getY()).put("footBallModel", footBallModel));
        for (int i = 0; i < p1_chess_model_list.length; i++) {
            FXGL.spawn("Chess", new SpawnData(p1_chess_model_list[i].getX(), p1_chess_model_list[i].getY())
                    .put("chessModel", p1_chess_model_list[i]));
        }
        for (int i = 0; i < p2_chess_model_list.length; i++) {
            FXGL.spawn("Chess", new SpawnData(p2_chess_model_list[i].getX(), p2_chess_model_list[i].getY())
                    .put("chessModel", p2_chess_model_list[i]));
        }

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