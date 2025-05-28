package com.tkuimwd.event;

import javafx.event.Event;
import javafx.event.EventType;

public class GoalEvent extends Event {

    public static final EventType<GoalEvent> GOAL = new EventType<>(Event.ANY, "GOAL");
    private final String id; // 球門ID

    public GoalEvent(String id) {
        super(GOAL);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
