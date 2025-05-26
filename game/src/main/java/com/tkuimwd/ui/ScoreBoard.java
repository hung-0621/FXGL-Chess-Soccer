package com.tkuimwd.ui;

import com.tkuimwd.Config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.tkuimwd.api.dto.MatchData;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ScoreBoard {

    private final double WIDTH;
    private final double HEIGHT;
    private final String player1_name;
    private final String player2_name;

    private MatchData matchData;
    // private int player1_score;
    // private int player2_score;
    // private boolean player1_turn;
    // private boolean player2_turn;

    private Text p1_name;
    private Text p2_name;
    private Text p1_score;
    private Text p2_score;
    private Text p1_turn;
    private Text p2_turn;
    private Text vs;

    public ScoreBoard(double width, double height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.matchData = Config.matchData;
        this.player1_name = Config.player1_name;
        this.player2_name = Config.player2_name;

        // this.player1_score = matchData.getScore1();
        // this.player2_score = matchData.getScore2();

        // this.player1_turn = Config.isHost && Config.isMyTurn;
        // this.player2_turn = !Config.isHost && Config.isMyTurn;
    }

    public void CreateScoreBoard() {
        // score board
        p1_name = FXGL.getUIFactoryService().newText(player1_name, Color.WHITE, 30);
        p2_name = FXGL.getUIFactoryService().newText(player2_name, Color.WHITE, 30);
        p1_score = FXGL.getUIFactoryService().newText("", Color.WHITE, 40);
        p2_score = FXGL.getUIFactoryService().newText("", Color.WHITE, 40);
        p1_turn = FXGL.getUIFactoryService().newText("", Color.WHITE, 20);
        p2_turn = FXGL.getUIFactoryService().newText("", Color.WHITE, 20);

        vs = FXGL.getUIFactoryService().newText(" VS ", Color.WHITE, 30);

        // main
        HBox pane = new HBox();
        pane.setSpacing(40);
        pane.setMinSize(WIDTH, HEIGHT);
        pane.setAlignment(Pos.CENTER);
        String css = """
                -fx-background-color:linear-gradient(to right,#1E90FF,#B22222);
                -fx-stroke: white;
                -fx-stroke-width: 15;
                """;
        pane.setStyle(css);
        pane.getChildren().addAll(p1_turn, p1_name, p1_score, vs, p2_score, p2_name, p2_turn);

        FXGL.addUINode(pane, 0, 0);

        updateScoreBoard();
    }

    public void updateScoreBoard() {
        p1_name.setText(player1_name);
        p2_name.setText(player2_name);
        p1_score.setText("" + Config.matchData.getScore1());
        p2_score.setText("" + Config.matchData.getScore2());
        vs.setText(" VS ");

        if (Config.isHost && Config.isMyTurn) {
            p1_turn.setText("Your Turn");
            p2_turn.setText("Waiting...");
        } else if (Config.isHost && !Config.isMyTurn) {
            p1_turn.setText("Waiting...");
            p2_turn.setText("Your Turn");

        } else if (!Config.isHost && Config.isMyTurn) {
            p1_turn.setText("Waiting...");
            p2_turn.setText("Your Turn");

        } else if (!Config.isHost && !Config.isMyTurn) {
            p1_turn.setText("Your Turn");
            p2_turn.setText("Waiting...");
        }
    }

    public void showGoal(){
        p1_name.setText("");
        p2_name.setText("");
        p1_score.setText("");
        p2_score.setText("");
        p1_turn.setText("");
        p2_turn.setText("");
        vs.setText("GOAL");
    }
}
