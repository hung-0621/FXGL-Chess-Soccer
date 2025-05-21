package com.tkuimwd.ui;

import com.tkuimwd.Config;

import com.almasb.fxgl.dsl.FXGL;
import com.tkuimwd.api.dto.MatchData;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class ScoreBoard {

    private final double WIDTH;
    private final double HEIGHT;
    private final String player1_name;
    private final String player2_name;
    private MatchData matchData;
    private int player1_score;
    private int player2_score;

    public ScoreBoard(double width, double height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.matchData = Config.matchData;
        this.player1_name = Config.player1_name;
        this.player2_name = Config.player2_name;
        this.player1_score = matchData.getScore1();
        this.player2_score = matchData.getScore2();
    }

    public void CreateScoreBoard() {
        // score board
        Text p1_name = FXGL.getUIFactoryService().newText(player1_name, Color.BLUE, 30);
        Text p2_name = FXGL.getUIFactoryService().newText(player2_name, Color.RED, 30);
        Text p1_score = FXGL.getUIFactoryService().newText("" + player1_score, Color.WHITE, 40);
        Text p2_score = FXGL.getUIFactoryService().newText("" + player2_score, Color.WHITE, 40);
        Text vs = FXGL.getUIFactoryService().newText(" VS ", Color.WHITE, 30);

        vs.setFill(Color.WHITE);

        // main
        HBox pane = new HBox();
        pane.setSpacing(30);
        pane.setMinSize(WIDTH, HEIGHT);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-font-size: 20; -fx-border: rgb(0,0,0)");
        pane.getChildren().addAll(p1_name, p1_score, vs, p2_score, p2_name);

        FXGL.addUINode(pane, 0, 0);
    }

}
