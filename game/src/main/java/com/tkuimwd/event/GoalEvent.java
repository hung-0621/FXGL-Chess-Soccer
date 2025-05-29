package com.tkuimwd.event;

import com.almasb.fxgl.entity.Entity;
import com.tkuimwd.component.GoalComponent;

import javafx.event.Event;
import javafx.event.EventType;

public class GoalEvent extends Event {

    public static final EventType<GoalEvent> GOAL = new EventType<>(Event.ANY, "GOAL");
    private final String id; // 球門ID
    private final GoalComponent goal;

    public GoalEvent(GoalComponent goal, String id) {
        super(GOAL);
        this.goal = goal;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public GoalComponent getGoal() {
        return goal;
    }
}
