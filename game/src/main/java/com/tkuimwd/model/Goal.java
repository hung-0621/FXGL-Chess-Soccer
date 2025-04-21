package com.tkuimwd.model;

import javafx.geometry.Point2D;

public class Goal {
    private Point2D position;
    private double width;
    private double height;

    public Goal(Point2D position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
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
}
