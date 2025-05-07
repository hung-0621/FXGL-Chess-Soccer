package com.exp.server.service.simulation.dto;

public class MoveCommand {
    String id;
    String session_id;
    double start_x, start_y;
    double end_x, end_y;

    public MoveCommand(String id, String session_id, double start_x, double start_y, double end_x, double end_y) {
        this.id = id;
        this.session_id = session_id;
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;
    }

    public String getId() {
        return id;
    }

    public String getSessionId() {
        return session_id;
    }

    public double getStartX() {
        return start_x;
    }

    public double getStartY() {
        return start_y;
    }

    public double getEndX() {
        return end_x;
    }

    public double getEndY() {
        return end_y;
    }
}