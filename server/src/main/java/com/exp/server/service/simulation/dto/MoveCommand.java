package com.exp.server.service.simulation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveCommand {
    
    @JsonProperty("id")
    private String id;

    @JsonProperty("startX")
    private double startX;

    @JsonProperty("startY")
    private double startY;

    @JsonProperty("endX")
    private double endX;

    @JsonProperty("endY")
    private double endY;

    public MoveCommand() {
        
    }

    public MoveCommand(String id, double startX, double startY, double endX, double endY) {
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public String getId() {
        return id;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

}