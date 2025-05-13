package com.tkuimwd.model;

import com.tkuimwd.type.Role;

import javafx.geometry.Point2D;

public class ChessModel {

    private String id;
    private Point2D point;
    private Role role;
    private int size;

    public ChessModel(String id, Point2D point, Role role){
        this.id = id;
        this.point = point;
        this.role = role;
        this.size = 20; // Default size
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public Role getRole() {
        return this.role;
    }

    public int getSize() {
        return this.size;
    }
}
