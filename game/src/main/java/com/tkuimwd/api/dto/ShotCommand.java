package com.tkuimwd.api.dto;

public class ShotCommand {
    private final String type;
    private final String matchId;
    int tick;
    MoveCommand commands;

    public ShotCommand(int tick, String type, String matchId, MoveCommand commands) {
        this.tick = tick;
        this.type = type;
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
