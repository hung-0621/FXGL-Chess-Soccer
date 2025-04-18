package com.tkuimwd.model;

import com.tkuimwd.type.Role;

public class ChessModel {

    private Role role;
    private int size = 20;

    public ChessModel(Role role){
        this.role = role;

    }

    public ChessModel(Role role, int size){
        this.role = role;
        this.size = size;
    }

    public Role getRole() {
        return this.role;
    }

    public int getSize() {
        return this.size;
    }
}
