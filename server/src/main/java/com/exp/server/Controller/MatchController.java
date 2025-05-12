package com.exp.server.Controller;

import com.exp.server.model.MatchModel;
import com.exp.server.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/match")
public class MatchController {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchController(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMatch(@RequestBody MatchModel match) {
        // 驗證必要欄位是否存在
        if (match.getRoomId() == null || match.getRoomId().isBlank() ||
            match.getPlayer1Id() == null || match.getPlayer1Id().isBlank() ||
            match.getPlayer2Id() == null || match.getPlayer2Id().isBlank() ||
            match.getMatchStatus() == null || match.getMatchStatus().isBlank()) {
            return ResponseEntity.badRequest().body("缺少必要欄位（room_Id, player1Id, player2Id, matchStatus）");
        }

        // 隨機指定 currentPlayerId
        String firstPlayerId = Math.random() < 0.5 ? match.getPlayer1Id() : match.getPlayer2Id();
        match.setCurrentPlayerId(firstPlayerId);

        match.setStartedAt(LocalDateTime.now());
        match.setMatchStatus("playing");
        // match.setMatchStatus(match.getMatchStatus());
        MatchModel saved = matchRepository.save(match);
        return ResponseEntity.ok(saved);
    }

    // 查詢指定對局
    @GetMapping("/{id}")
    public ResponseEntity<MatchModel> getMatch(@PathVariable String id) {
        Optional<MatchModel> match = matchRepository.findById(id);
        if (match.isPresent()) {
            return new ResponseEntity<>(match.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 更新指定對局
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMatch(@PathVariable String id, @RequestBody MatchModel match) {
        if (!matchRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        match.setId(id);
        matchRepository.save(match);
        return new ResponseEntity<>("對局已成功更新", HttpStatus.OK);
    }

    // 刪除指定對局
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatch(@PathVariable String id) {
        if (!matchRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        matchRepository.deleteById(id);
        return new ResponseEntity<>("對局已成功刪除", HttpStatus.NO_CONTENT);
    }

    // 根據 roomId 查詢對局
    @GetMapping("/room/{roomId}")
    public ResponseEntity<MatchModel> getMatchByRoomId(@PathVariable String roomId) {
        Optional<MatchModel> match = matchRepository.findByRoomId(roomId);
        return match.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
