package com.tkuimwd.api.dto;

public class MatchData {

    private String id;
    private String roomId;
    private String player1Id;
    private String player2Id;
    private int    score1;
    private int    score2;
    private String winnerId;
    private String startedAt;
    private String endedAt;
    private String currentPlayerId;
    private String matchStatus;
    private boolean waitingForTurnSwitch;
    private boolean hasMovedThisTurn;

    // 1) 一定要有 public no-args constructor
    public MatchData() { }

    // 2) getters + setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getPlayer1Id() { return player1Id; }
    public void setPlayer1Id(String player1Id) { this.player1Id = player1Id; }

    public String getPlayer2Id() { return player2Id; }
    public void setPlayer2Id(String player2Id) { this.player2Id = player2Id; }

    public int getScore1() { return score1; }
    public void setScore1(int score1) { this.score1 = score1; }

    public int getScore2() { return score2; }
    public void setScore2(int score2) { this.score2 = score2; }

    public String getWinnerId() { return winnerId; }
    public void setWinnerId(String winnerId) { this.winnerId = winnerId; }

    public String getStartedAt() { return startedAt; }
    public void setStartedAt(String startedAt) { this.startedAt = startedAt; }

    public String getEndedAt() { return endedAt; }
    public void setEndedAt(String endedAt) { this.endedAt = endedAt; }

    public String getCurrentPlayerId() { return currentPlayerId; }
    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public String getMatchStatus() { return matchStatus; }
    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public boolean isWaitingForTurnSwitch() {
        return waitingForTurnSwitch;
    }
    public void setWaitingForTurnSwitch(boolean waitingForTurnSwitch) {
        this.waitingForTurnSwitch = waitingForTurnSwitch;
    }

    public boolean isHasMovedThisTurn() {
        return hasMovedThisTurn;
    }
    public void setHasMovedThisTurn(boolean hasMovedThisTurn) {
        this.hasMovedThisTurn = hasMovedThisTurn;
    }

    @Override
    public String toString() {
        return "MatchData{" +
                "id='" + id + '\'' +
                ", roomId='" + roomId + '\'' +
                ", player1Id='" + player1Id + '\'' +
                ", player2Id='" + player2Id + '\'' +
                ", score1=" + score1 +
                ", score2=" + score2 +
                ", winnerId='" + winnerId + '\'' +
                ", startedAt='" + startedAt + '\'' +
                ", endedAt='" + endedAt + '\'' +
                ", currentPlayerId='" + currentPlayerId + '\'' +
                ", matchStatus='" + matchStatus + '\'' +
                ", waitingForTurnSwitch=" + waitingForTurnSwitch +
                ", hasMovedThisTurn=" + hasMovedThisTurn +
                '}';
    }
}
