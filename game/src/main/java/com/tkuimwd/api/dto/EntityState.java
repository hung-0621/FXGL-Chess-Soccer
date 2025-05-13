package com.tkuimwd.api.dto;

import com.tkuimwd.type.EntityType;

// /api/game/state

public class EntityState {
    String id;
    // String session_id; //!
    // EntityType type; //!
    double x;
    double y;
    // double vx;
    // double vy;

    // public EntityState(String id, EntityType type, double x, double y, double vx, double vy) {
    //     this.id = id;
    //     this.type = type;
    //     this.x = x;
    //     this.y = y;
    //     this.vx = vx;
    //     this.vy = vy;
    // }

    // websocket
    public EntityState(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        // this.vx = vx;
        // this.vy = vy;
    }

    public String getId() {
        return id;
    }

    // public String getSessionId() {
    //     return session_id;
    // }

    // public EntityType getType() {
    //     return type;
    // }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // public double getVx() {
    //     return vx;
    // }

    // public double getVy() {
    //     return vy;
    // }

    public void setId(String id) {
        this.id = id;
    }

    // public void setType(EntityType type) {
    //     this.type = type;
    // }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    // public void setVx(double vx) {
    //     this.vx = vx;
    // }

    // public void setVy(double vy) {
    //     this.vy = vy;
    // }

    @Override
    public String toString() {
        return "EntityState{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                // ", vx=" + vx +
                // ", vy=" + vy +
                '}';
    }
}
