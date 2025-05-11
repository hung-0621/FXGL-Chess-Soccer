package com.tkuimwd.api.dto;

import java.util.List;

public class ShotCommand {
    private final String type;
    private final String matchId;
    int tick;
    MoveCommand commands;

    public ShotCommand(int tick, String matchId, MoveCommand commands) {
        this.tick = tick;
        this.type = "shot";
        this.matchId = matchId;
        this.commands = commands;
    }

    public int getTick() {
        return tick;
    }   

    public String getType() {
        return type;
    }

    public MoveCommand getCommand() {
        return commands;
    }

    public String getMatchId() {
        return matchId;
    }
}
