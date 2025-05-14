package com.tkuimwd.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateUpdate {

    @JsonProperty("type")
    private final String type = "state_update";

    @JsonProperty("tick")
    private int tick;

    @JsonProperty("matchId")
    private String matchId;

    @JsonProperty("payload")
    private List<EntityState> states;

    public StateUpdate() {

    }

    public StateUpdate(int tick, String matchId, List<EntityState> states) {
        this.tick = tick;
        this.matchId = matchId;
        this.states = states;
    }

    public String getType() {
        return type;
    }

    public int getTick() {
        return tick;
    }

    public String getMatchId() {
        return matchId;
    }

    public List<EntityState> getStates() {
        return states;
    }
}