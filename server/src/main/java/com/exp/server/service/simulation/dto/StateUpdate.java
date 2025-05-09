package com.exp.server.service.simulation.dto;

import java.util.List;

public class StateUpdate {
    int seq;
    List<EntityState> states;

    public StateUpdate(int seq, List<EntityState> states) {
        this.seq = seq;
        this.states = states;
    }

    public int getSeq() {
        return seq;
    }

    public List<EntityState> getStates() {
        return states;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("StateUpdate{seq=").append(seq).append(", states=[");
        for (EntityState state : states) {
            sb.append(state.toString()).append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }
}
