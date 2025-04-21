package com.tkuimwd.model;

import javafx.geometry.Point2D;

public class FootBallModel {
    
    private Point2D position;
    private int size;
    private int speed;

    public FootBallModel(Point2D position) {
        this.position = position;
        this.size = 12; // Default size
        this.speed = 0;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public int getSize() {
        return size;
    }

    public int getSpeed() {
        return speed;
    }
}
