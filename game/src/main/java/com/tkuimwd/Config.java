package com.tkuimwd;

import javafx.geometry.Point2D;

public class Config {
    public static final int HEIGHT = 748; // 遊戲場景高度 scene + board
    public static final int WIDTH = 1020; // 遊戲場景寬度
    static final String TITLE = "Chess Soccer"; // 遊戲標題
    
    // background init
    static final Point2D BACKGROUND_POSITION = new Point2D(0, 70);
    static final String IMAGE_PATH = "/field.jpg"; // 遊戲場景背景圖片路徑

    // wall 的初始位置
    public static final Point2D WALL_POSITION= new Point2D(0, 70);
    public static final double[][] WALL_EDGES = {
            { 61, 54 }, { 61, 254  }, { 29, 254 }, { 29, 433 },
            { 61, 433 }, { 61, 651 }, { 958, 651 }, { 958, 433 },
            { 991, 433 }, { 991, 254 }, { 958, 254 }, { 958, 54 },
    };

    // football 的初始位置
    public static final Point2D FOOTBALL_POSITION = new Point2D(511, 423);

    // chess 的初始位置
    public static double[][] player1_chess_position = {
            { 160, 420 }, { 265, 305 }, { 265, 520 },
    };
    public static double[][] player2_chess_position = {
            { 850, 420 }, { 750, 305 }, { 750, 520 },
    };
    static final Point2D[] P1_CHESS_POSITION = new Point2D[player1_chess_position.length];
    static final Point2D[] P2_CHESS_POSITION = new Point2D[player2_chess_position.length];
    static {
        for (int i = 0; i < player1_chess_position.length; i++) {
            P1_CHESS_POSITION[i] = new Point2D(player1_chess_position[i][0], player1_chess_position[i][1]);
        }
        for (int i = 0; i < player2_chess_position.length; i++) {
            P2_CHESS_POSITION[i] = new Point2D(player2_chess_position[i][0], player2_chess_position[i][1]);
        }
    }
    // Goal 的初始位置
    static final double GOAL_WIDTH = 32; // 球門寬度
    static final double GOAL_HEIGHT = 179; // 球門高度
    static final Point2D P1_GOAL_POSITION = new Point2D(29, 324);
    static final Point2D P2_GOAL_POSITION = new Point2D(958, 324);

    // force
    public static final double MAX_FORCE = 200; // 可施加最大的力
    public static final double MAX_DISTANCE = 100; // 可拖曳的最大距離
}
