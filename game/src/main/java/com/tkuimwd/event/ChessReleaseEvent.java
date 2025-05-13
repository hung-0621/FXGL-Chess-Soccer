package com.tkuimwd.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * 拖曳棋子放手時派發的事件，
 * 會攜帶 startX/Y, endX/Y 四個座標。
 */
public class ChessReleaseEvent extends Event {

    /** 事件類型，用於訂閱 */
    public static final EventType<ChessReleaseEvent> CHESS_RELEASE = new EventType<>(Event.ANY, "CHESS_RELEASE");

    private final String id;
    private final double startX, startY, endX, endY;

    public ChessReleaseEvent(String id, double startX, double startY,
            double endX, double endY) {
        super(CHESS_RELEASE);
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
}
