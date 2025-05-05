package com.tkuimwd.model;

import com.tkuimwd.type.Role;

public class ScoreBoardModel {
    private int p1_score;
    private int p2_score;

    public ScoreBoardModel() {
        this.p1_score = 0;
        this.p2_score = 0;
    }

    public int getScore(Role role) {
        return role.equals(Role.PLAYER1) ? p1_score : p2_score;
    }

    public void setScore(Role role, int score) {
        if (role.equals(Role.PLAYER1)) {
            this.p1_score = score;
        } else {
            this.p2_score = score;
        }
    }

}
