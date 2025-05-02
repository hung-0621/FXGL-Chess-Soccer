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

import com.exp.server.model.RoomModel;
import com.exp.server.repository.RoomRepository;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;


    
    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody Map<String, String> body) {
        String hostToken = body.get("token");

        if (hostToken == null || hostToken.isBlank()) {
            return ResponseEntity.badRequest().body("請提供 token");
        }

        RoomModel room = new RoomModel();
        room.setHostToken(hostToken);
        room.setStatus("waiting");
        room.setRoomCode(generateRoomCode()); // 產生像 ABC123 的房號
        room.setCreatedAt(LocalDateTime.now());
        room.setGuestStatus("未準備");

        roomRepository.save(room);
        return ResponseEntity.ok(room);
    }

    private String generateRoomCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
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

}
