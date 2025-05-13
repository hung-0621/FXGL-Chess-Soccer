package com.tkuimwd.api.dto;

import java.util.List;

public class State {
    private final String type;
    private final String matchId;
    int tick;
    List<EntityState> states;

    public State(int tick, String type, String matchId, List<EntityState> states) {
        this.tick = tick;
        this.type = type;
        this.matchId = matchId;
        this.states = states;
    }

    public int getTick() {
        return tick;
    }

    public String getType() {
        return type;
    }

    public List<EntityState> getStates() {
        return states;
    }

    public String getMatchId() {
        return matchId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StateUpdate{")
                .append("type='").append(type).append('\'')
                .append(", tick=").append(tick)
                .append(", states=[\n");

        if (states != null) {
            for (int i = 0; i < states.size(); i++) {
                sb.append("    ").append(states.get(i).toString()).append("\n");
            }
        }

        sb.append("]}");
        return sb.toString();
    }
}
