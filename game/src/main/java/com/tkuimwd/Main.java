package com.tkuimwd;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.ui.DialogFactoryService;
import com.tkuimwd.api.dto.MatchData;
import com.tkuimwd.component.NetworkComponent;
import com.tkuimwd.factory.BackgroundFactory;
import com.tkuimwd.factory.ChessFactory;
import com.tkuimwd.factory.FootBallFactory;
import com.tkuimwd.factory.WallFactory;
import com.tkuimwd.factory.GoalFactory;
import com.tkuimwd.factory.MenuFactory;
import com.tkuimwd.model.BackgroundModel;
import com.tkuimwd.model.FootBallModel;
import com.tkuimwd.model.GoalModel;
import com.tkuimwd.model.WallModel;
import com.tkuimwd.model.ChessModel;
import com.tkuimwd.type.EntityType;
import com.tkuimwd.type.Role;
import com.tkuimwd.ui.ScoreBoard;
import com.tkuimwd.util.MouseTracker;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class Main extends GameApplication {

    private static final int HEIGHT = Config.HEIGHT;
    private static final int WIDTH = Config.WIDTH;
    private static Main instance;
    private ScoreBoard scoreBoard;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("CHESS SOCCER");
        settings.setVersion("v1.1");
        settings.setDeveloperMenuEnabled(true);
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new MenuFactory());
    }

    @Override
    protected void initUI() {
        instance = this;
        this.scoreBoard = new ScoreBoard(WIDTH, 70);
        scoreBoard.CreateScoreBoard();
    }

    @Override
    protected void initGame() {
        // new MouseTracker().tracker();

        // config
        final double[][] WALL_EDGES = Config.WALL_EDGES;
        final String IMAGE_PATH = Config.IMAGE_PATH;
        final Point2D BACKGROUND_POSITION = Config.BACKGROUND_POSITION;
        final Point2D WALL_POSITION = Config.WALL_POSITION;
        final Point2D FOOTBALL_POSITION = Config.FOOTBALL_POSITION;
        final Point2D[] P1_CHESS_POSITION = Config.P1_CHESS_POSITION;
        final Point2D[] P2_CHESS_POSITION = Config.P2_CHESS_POSITION;
        final Point2D P1_GOAL_POSITION = Config.P1_GOAL_POSITION;
        final Point2D P2_GOAL_POSITION = Config.P2_GOAL_POSITION;
        final double GOAL_WIDTH = Config.GOAL_WIDTH;
        final double GOAL_HEIGHT = Config.GOAL_HEIGHT;

        // Factorys
        FXGL.getGameWorld().addEntityFactory(new BackgroundFactory());
        FXGL.getGameWorld().addEntityFactory(new WallFactory());
        FXGL.getGameWorld().addEntityFactory(new FootBallFactory());
        FXGL.getGameWorld().addEntityFactory(new ChessFactory());
        FXGL.getGameWorld().addEntityFactory(new GoalFactory());

        // Model
        BackgroundModel backgroundModel = new BackgroundModel(IMAGE_PATH);
        WallModel wallModel = new WallModel(WALL_EDGES);
        FootBallModel footBallModel = new FootBallModel("football", FOOTBALL_POSITION);
        ChessModel[] p1_chess_model_list = new ChessModel[P1_CHESS_POSITION.length];
        ChessModel[] p2_chess_model_list = new ChessModel[P2_CHESS_POSITION.length];
        GoalModel p1_goal_model = new GoalModel(P1_GOAL_POSITION, GOAL_WIDTH, GOAL_HEIGHT);
        GoalModel p2_goal_model = new GoalModel(P2_GOAL_POSITION, GOAL_WIDTH, GOAL_HEIGHT);

        for (int i = 0; i < P1_CHESS_POSITION.length; i++) {
            String id = "p1_chess_" + i;
            p1_chess_model_list[i] = new ChessModel(id, P1_CHESS_POSITION[i], Role.PLAYER1);
        }

        for (int i = 0; i < P2_CHESS_POSITION.length; i++) {
            String id = "p2_chess_" + i;
            p2_chess_model_list[i] = new ChessModel(id, P2_CHESS_POSITION[i], Role.PLAYER2);
        }

        // Spawn
        FXGL.spawn("Background", new SpawnData(BACKGROUND_POSITION).put("backgroundModel", backgroundModel));
        FXGL.spawn("Wall", new SpawnData(WALL_POSITION).put("wallModel", wallModel));
        FXGL.spawn("FootBall",
                new SpawnData(footBallModel.getX(), footBallModel.getY())
                        .put("footBallModel", footBallModel));
        for (int i = 0; i < p1_chess_model_list.length; i++) {
            FXGL.spawn("Chess", new SpawnData(p1_chess_model_list[i].getX(), p1_chess_model_list[i].getY())
                    .put("chessModel", p1_chess_model_list[i]));
        }
        for (int i = 0; i < p2_chess_model_list.length; i++) {
            FXGL.spawn("Chess", new SpawnData(p2_chess_model_list[i].getX(), p2_chess_model_list[i].getY())
                    .put("chessModel", p2_chess_model_list[i]));
        }
        FXGL.spawn("Goal", new SpawnData(P1_GOAL_POSITION).put("goalModel", p1_goal_model));
        FXGL.spawn("Goal", new SpawnData(P2_GOAL_POSITION).put("goalModel", p2_goal_model));

        initNetwork();

    }

    private void initNetwork() {
        FXGL.entityBuilder()
                .with(new NetworkComponent())
                .buildAndAttach();
    }

    @Override
    protected void initInput() {

    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 0);
    }

    public static ScoreBoard getScoreBoard() {
        return instance.scoreBoard;
    }

    public static void main(String[] args) {
        launch(args);
    }
}