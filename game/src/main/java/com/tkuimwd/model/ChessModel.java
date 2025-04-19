package com.tkuimwd.model;

import com.tkuimwd.type.Role;

public class ChessModel {

    private int x;
    private int y;
    private Role role;
    private int size;

    public ChessModel(int x, int y, Role role){
        this.x = x;
        this.y = y;
        this.role = role;
        this.size = 20; // Default size
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Role getRole() {
        return this.role;
    }

    public int getSize() {
        return this.size;
    }
}
