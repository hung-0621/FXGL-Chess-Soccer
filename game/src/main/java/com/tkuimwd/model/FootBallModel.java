package com.tkuimwd.model;

import javafx.geometry.Point2D;

public class FootBallModel {
    
    private Point2D position;
    private int radius;
    private int speed;

    public FootBallModel(Point2D position) {
        this.position = position;
        this.radius = 12; // Default radius
        this.speed = 0;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public int getRadius() {
        return radius;
    }

    public int getSpeed() {
        return speed;
    }
}
