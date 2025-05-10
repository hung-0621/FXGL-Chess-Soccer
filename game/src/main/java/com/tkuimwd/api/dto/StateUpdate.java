package com.tkuimwd.api.dto;

import java.util.List;

public class StateUpdate {
    private final String type;
    private final String matchId;
    int seq;
    List<EntityState> states;

    public StateUpdate(int seq, String type, String matchId, List<EntityState> states) {
        this.seq = seq;
        this.type = type;
        this.matchId = matchId;
        this.states = states;
    }

    public int getSeq() {
        return seq;
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
                .append(", seq=").append(seq)
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
