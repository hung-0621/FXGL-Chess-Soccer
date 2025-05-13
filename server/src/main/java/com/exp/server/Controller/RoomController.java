package com.exp.server.Controller;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.exp.server.model.MatchModel;
import com.exp.server.model.RoomModel;
import com.exp.server.repository.MatchRepository;
import com.exp.server.repository.RoomRepository;
import com.exp.server.websocket.GameWebSocketHandler_00;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MatchRepository matchRepository;


    
    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody Map<String, String> body) {
        String hostToken = body.get("token");

        if (hostToken == null || hostToken.isBlank()) {
            return ResponseEntity.badRequest().body("請提供 token");
        }

        //產生不重複的房號
        String roomCode;
        int maxAttempts = 10;
        int attempts = 0;
        do {
            roomCode = generateRoomCode();
            attempts++;
            if (attempts > maxAttempts) {
                return ResponseEntity.status(500).body("無法產生唯一房號，請稍後再試");
            }
        } while (roomRepository.findByRoomCode(roomCode) != null);

        RoomModel room = new RoomModel();
        room.setHostToken(hostToken);
        room.setStatus("waiting");
        room.setRoomCode(roomCode);
        room.setCreatedAt(LocalDateTime.now());
        room.setGuestStatus("未準備");

        roomRepository.save(room);
        return ResponseEntity.ok(room);
    }

    private String generateRoomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // 玩家加入房間(roomCode , token)
    @PostMapping("/join")
    public ResponseEntity<?> joinRoom(@RequestBody Map<String, String> body) {
        String roomCode = body.get("roomCode");
        String guestToken = body.get("token");

        RoomModel room = roomRepository.findByRoomCode(roomCode);
        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("房間不存在");
        }

        if (room.getGuestToken() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("房間已滿");
        }

        room.setGuestToken(guestToken);
        room.setStatus("ready");
        roomRepository.save(room);

        return ResponseEntity.ok("成功加入房間：" + roomCode);
    }

    // 取得房間資訊(roomCode)
    @GetMapping("/info")
    public ResponseEntity<?> getRoomInfo(@RequestParam("roomCode") String roomCode) {
        RoomModel room = roomRepository.findByRoomCode(roomCode);
        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("房間不存在");
        }
        return ResponseEntity.ok(room);
    }
    
    // 刪除房間(roomCode)
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRoom(@RequestParam("roomCode") String roomCode) {
        RoomModel room = roomRepository.findByRoomCode(roomCode);
        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("房間不存在");
        }
        roomRepository.delete(room);
        return ResponseEntity.ok("房間已刪除");
    }

        // 更新房間資訊
    @PutMapping("/update")
    public ResponseEntity<?> updateRoom(@RequestBody Map<String, Object> body) {
        String roomCode = (String) body.get("roomCode");
        if (roomCode == null || roomCode.isBlank()) {
            return ResponseEntity.badRequest().body("請提供 roomCode");
        }

        RoomModel room = roomRepository.findByRoomCode(roomCode);
        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("房間不存在");
        }

        if (body.containsKey("guestToken")) {
            room.setGuestToken((String) body.get("guestToken"));
        }
        if (body.containsKey("status")) {
            room.setStatus((String) body.get("status"));
        }
        if (body.containsKey("matchId")) {
            Object matchIdObj = body.get("matchId");
            if (matchIdObj instanceof String) {
                room.setMatchId((String) matchIdObj);
            }
        }

        roomRepository.save(room);
        return ResponseEntity.ok(room);
    }

    // Guest 玩家按下「我準備好了」
    @PostMapping("/ready")
    public ResponseEntity<?> guestReady(@RequestBody Map<String, String> body) {
        String roomCode = body.get("roomCode");
        RoomModel room = roomRepository.findByRoomCode(roomCode);

        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("房間不存在");
        }

        if (room.getGuestToken() == null || !room.getGuestToken().equals(body.get("token"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("不是這個房間的 guest");
        }

        room.setGuestStatus("準備");
        roomRepository.save(room);

        return ResponseEntity.ok("guest 已準備");
    }

    @PostMapping("/start")
    public ResponseEntity<?> startMatch(@RequestBody Map<String, String> body) {
        String roomCode = body.get("roomCode");
        String hostToken = body.get("token");

        RoomModel room = roomRepository.findByRoomCode(roomCode);
        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("房間不存在");
        }

        if (!room.getHostToken().equals(hostToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("只有房主可以開始遊戲");
        }

        if (!"準備".equals(room.getGuestStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("guest 尚未準備好");
        }

        if ("finished".equals(room.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("此房間的對戰已結束，無法重新開始");
        }


    // 建立對局資料（呼叫 MatchController.create）
    MatchModel match = new MatchModel();
    match.setRoomId(room.getRoomCode());
    match.setPlayer1Id(room.getHostToken());
    match.setPlayer2Id(room.getGuestToken());
    match.setMatchStatus("playing");

    // 隨機先手
    String current = Math.random() < 0.5 ? match.getPlayer1Id() : match.getPlayer2Id();
    match.setCurrentPlayerId(current);
    match.setStartedAt(LocalDateTime.now());

    MatchModel savedMatch = matchRepository.save(match);
    room.setMatchId(savedMatch.getId());
    room.setStatus("playing");
    roomRepository.save(room);

        String gameStartMsg = String.format(
        "{\"type\":\"game_start\",\"matchId\":\"%s\",\"yourTurn\":true}",
        savedMatch.getId()
    );

        String guestMsg = String.format(
        "{\"type\":\"game_start\",\"matchId\":\"%s\",\"yourTurn\":false}",
        savedMatch.getId()
    );

    if (savedMatch.getCurrentPlayerId().equals(savedMatch.getPlayer1Id())) {
        GameWebSocketHandler_00.sendToToken(savedMatch.getPlayer1Id(), gameStartMsg);
        GameWebSocketHandler_00.sendToToken(savedMatch.getPlayer2Id(), guestMsg);
    } else {
        GameWebSocketHandler_00.sendToToken(savedMatch.getPlayer2Id(), gameStartMsg);
        GameWebSocketHandler_00.sendToToken(savedMatch.getPlayer1Id(), guestMsg);
    }

    return ResponseEntity.ok(savedMatch);
    }

}
