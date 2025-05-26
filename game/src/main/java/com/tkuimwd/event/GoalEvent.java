package com.tkuimwd.event;

import javafx.event.Event;
import javafx.event.EventType;

public class GoalEvent extends Event {
    
    public static final EventType<GoalEvent> GOAL = new EventType<>(Event.ANY, "GOAL");

    public GoalEvent() {
        super(GOAL);
    }
}
