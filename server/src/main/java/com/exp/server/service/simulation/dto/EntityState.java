package com.exp.server.service.simulation.dto;

import com.exp.server.service.simulation.dto.type.EntityType;

public class EntityState {
    int id;
    EntityType type;
    double x;
    double y;
    double vx;
    double vy;

    public EntityState(int id, EntityType type, double x, double y, double vx, double vy) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public int getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

}
