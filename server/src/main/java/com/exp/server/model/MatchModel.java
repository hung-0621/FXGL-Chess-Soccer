package com.exp.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "Matches")
public class MatchModel {
    @Id
    private String id;
    private String roomId;
    private String player1Id;
    private String player2Id;
    private int score1;
    private int score2;
    private String winnerId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String currentPlayerId;
    private String matchStatus;
    // 表示目前是否在等待實體靜止來切換回合
    private boolean waitingForTurnSwitch = false;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getEndedAt() { return endedAt; }
    public void setEndedAt(LocalDateTime endedAt) { this.endedAt = endedAt; }

    public String getCurrentPlayerId() { return currentPlayerId; }
    public void setCurrentPlayerId(String currentPlayerId) { this.currentPlayerId = currentPlayerId; }

    public String getMatchStatus() { return matchStatus; }
    public void setMatchStatus(String matchStatus) { this.matchStatus = matchStatus; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String room_Id) { this.roomId = room_Id;}

    public boolean isWaitingForTurnSwitch() {
        return waitingForTurnSwitch;}
    public void setWaitingForTurnSwitch(boolean value) {
        this.waitingForTurnSwitch = value;}

}
