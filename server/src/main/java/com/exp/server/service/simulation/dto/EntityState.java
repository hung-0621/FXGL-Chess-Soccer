package com.exp.server.service.simulation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.exp.server.service.simulation.dto.type.EntityType;

public class EntityState {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private EntityType type;

    @JsonProperty("x")
    private double x;

    @JsonProperty("y")
    private double y;

    @JsonProperty("vx")
    private double vx;

    @JsonProperty("vy")
    private double vy;

    public EntityState(String id, EntityType type, double x, double y, double vx, double vy) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    // websocket 專用無 type 建構式
    public EntityState(String id, double x, double y, double vx, double vy) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public String getId() {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }
}