package com.exp.server.service.simulation;

public class EntityConfig {
    // Entity position
    public static final double[] BACKGROUND_POSITION = {0.0, 70.0}; // {x, y}
    public static final double[] WALL_POSITION = {0.0, 70.0};
    public static final double[] FOOTBALL_POSITION = {511.0, 423.0};
    public static final double[][] GOAL_POSITION = {{29.0, 324.0}, {958.0, 324.0}}; // {{x1, y1},{x2, x2}}
    public static final double[][] P1_CHESS_POSITION = {{160.0, 420.0}, {265.0, 305.0}, {265.0, 520.0}}; // {{x1, y1}, {x2, y2}, {x3, y3}}
    public static final double[][] P2_CHESS_POSITION = {{850.0, 420.0}, {750.0, 305.0}, {750.0, 520.0}}; // {{x1, y1}, {x2, y2}, {x3, y3}}

    public static final float PPM = 40.0f;
}
