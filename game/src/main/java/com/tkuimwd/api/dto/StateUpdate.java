package com.tkuimwd.api.dto;

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
}
