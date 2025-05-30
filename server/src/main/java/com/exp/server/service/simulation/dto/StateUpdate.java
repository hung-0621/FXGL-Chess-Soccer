package com.exp.server.service.simulation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateUpdate {

    @JsonProperty("type")
    private final String type = "state_update";

    @JsonProperty("tick")
    private int seq;

    @JsonProperty("matchId")
    private String matchId;

    @JsonProperty("payload")
    private List<EntityState> states;

    public StateUpdate(){

    }

    public StateUpdate(int seq, List<EntityState> states) {
        this.seq = seq;
        this.states = states;
    }

    public String getType() {
        return type;
    }

    public int getSeq() {
        return seq;
    }

    public String getMatchId() {
        return matchId;
    }

    public List<EntityState> getStates() {
        return states;
    }
}