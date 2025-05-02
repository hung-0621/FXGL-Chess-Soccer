package com.exp.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "Rooms")
public class RoomModel {
    @Id
    private String id;
    private String roomCode;
    private String matchId;
    private String hostToken;
    private String guestToken;
    private String guestStatus;
    private String status;
    private LocalDateTime createdAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public String getHostToken() { return hostToken; }
    public void setHostToken(String hostToken) { this.hostToken = hostToken; }

    public String getGuestToken() { return guestToken; }
    public void setGuestToken(String guestToken) { this.guestToken = guestToken; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getMatchId() { return matchId; }
    public void setMatchId(String match_Id) { this.matchId = matchId;}

    public String getGuestStatus() { return guestStatus; }
    public void setGuestStatus(String guestStatus) { this.guestStatus = guestStatus;}
    
}
