package com.tkuimwd.model;

public class PlayerModel {
    
    private String name;
    private int score;

    public PlayerModel(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
