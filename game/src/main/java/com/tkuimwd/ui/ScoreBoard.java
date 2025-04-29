package com.tkuimwd.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.tkuimwd.model.ScoreBoardModel;
import com.tkuimwd.type.Role;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class ScoreBoard {

    private final double WIDTH;
    private final double HEIGHT;

    public ScoreBoard(double width, double height) {
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    public void CreateScoreBoard() {
        // score board
        ScoreBoardModel scoreBoard = new ScoreBoardModel();
        Text p1_name = new Text("Player 1");
        Text p2_name = new Text("Player 2");
        Text p1_score = new Text("" + scoreBoard.getScore(Role.PLAYER1));
        Text p2_score = new Text("" + scoreBoard.getScore(Role.PLAYER2));
        Text vs = new Text(" - ");

        p1_name.setFill(Color.BLUE);
        p2_name.setFill(Color.RED);
        p1_score.setFill(Color.WHITE);
        p2_score.setFill(Color.WHITE);
        vs.setFill(Color.WHITE);

        p1_name.setFont(Font.font("Bold", 30));
        p2_name.setFont(Font.font("Bold", 30));
        p1_score.setFont(Font.font("Bold", 50));
        p2_score.setFont(Font.font("Bold", 50));
        vs.setFont(Font.font("Bold", 30));

        // main
        HBox pane = new HBox();
        pane.setSpacing(30);
        pane.setMinSize(WIDTH, HEIGHT);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-font-size: 20; -fx-border: rgb(0,0,0)");
        pane.getChildren().addAll(p1_name, p1_score, vs, p2_score, p2_name);

        FXGL.addUINode(pane, 0, 0);
    }

    public void UpdateScoreBoard() {

    }
}
