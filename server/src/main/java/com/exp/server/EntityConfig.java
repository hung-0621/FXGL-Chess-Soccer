package com.exp.server;

public class EntityConfig {
    // Entity position (px)，後端使用要轉換成(m)
    public static final double[] BACKGROUND_POSITION = {0.0, 70.0}; // {x, y}
    public static final double[] WALL_POSITION = {0.0, 70.0};
    public static final double[] FOOTBALL_POSITION = {511.0, 423.0};
    public static final double[][] GOAL_POSITION = {{29.0, 324.0}, {958.0, 324.0}}; // {{x1, y1},{x2, x2}}
    public static final double[][] P1_CHESS_POSITION = {{160.0, 420.0}, {265.0, 305.0}, {265.0, 520.0}}; // {{x1, y1}, {x2, y2}, {x3, y3}}
    public static final double[][] P2_CHESS_POSITION = {{850.0, 420.0}, {750.0, 305.0}, {750.0, 520.0}}; // {{x1, y1}, {x2, y2}, {x3, y3}}

    public static final float PPM = 40.0f;

    public static final int SCENE_WIDTH = 1020; // 世界寬度
    public static final int SCENE_HEIGHT = 678; // 世界高度 (only scene no board)

    // 牆壁頂點座標 (px)，後端使用要轉換成(m)
    public static final float[][] WALL_EDGES = {
        { 61, 54 }, { 61, 254 }, { 29, 254 }, { 29, 433 },
        { 61, 433 }, { 61, 651 }, { 958, 651 }, { 958, 433 },
        { 991, 433 }, { 991, 254 }, { 958, 254 }, { 958, 54 },
    };
}
