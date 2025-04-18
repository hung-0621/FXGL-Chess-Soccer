package com.tkuimwd.model;

public class FootBallModel {
    
    private int x;
    private int y;
    private int size;
    private int speed;

    public FootBallModel() {
        this.x = 0;
        this.y = 0;
        this.size = 12; // Default size
        this.speed = 0;
    }

    public FootBallModel(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 12; // Default size
        this.speed = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public int getSpeed() {
        return speed;
    }
}
