package com.tkuimwd.model;

import javafx.geometry.Point2D;

public class GoalModel {
    private String id;
    private Point2D position; // 左上座標
    private double width;
    private double height;

    public GoalModel(String id, Point2D position, double width, double height) {
        this.id = id;
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public Point2D getPosition() {
        return position;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // public boolean isBallInGoal(FootBallModel football) {
    //     double ballX = football.getX();
    //     double ballY = football.getY();
    //     double ballRadius = football.getRadius();

    //     if (ballX + ballRadius <= position.getX() + width && ballX - ballRadius >= position.getX()) {
    //         if (ballY + ballRadius <= position.getY() + height && ballY - ballRadius >= position.getY()) {
    //             return true; // 球在球門內
    //         } else {
    //             return false; // 球在球門外
    //         }
    //     } else {
    //         return false; // 球在球門外
    //     }
    // }

}
