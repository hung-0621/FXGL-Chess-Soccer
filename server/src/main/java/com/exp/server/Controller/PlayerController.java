package com.exp.server.Controller;

import com.exp.server.model.PlayerModel;
import com.exp.server.repository.PlayerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // 取得所有玩家
    @GetMapping
    public List<PlayerModel> getAllPlayers() {
        return playerRepository.findAll();
    }

    // ID 取得單一玩家
    @GetMapping("findOne/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable String id) {
        Optional<PlayerModel> player = playerRepository.findById(id);
        if (player.isPresent()) {
            return ResponseEntity.ok(player.get());
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("查無此 ID：" + id);
        }
    }

    // 註冊
    @PostMapping("/register")
    public ResponseEntity<?> registerPlayer(@RequestBody PlayerModel player) {

        if (player.getUserName() == null || player.getUserName().isBlank() ||
            player.getEmail() == null || player.getEmail().isBlank() ||
            player.getPassword() == null || player.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("請提供完整的 userName、email 和 password");
        }

        if (playerRepository.findByEmail(player.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("此 email 已存在，請使用其他 email");
        }

        player.setPassword(passwordEncoder.encode(player.getPassword()));

        PlayerModel savedPlayer = playerRepository.save(player);
        return ResponseEntity.ok(savedPlayer);
    }

    // 登入
    @PostMapping("/login")
    public ResponseEntity<?> loginPlayer(@RequestBody PlayerModel loginRequest) {
        PlayerModel player = playerRepository.findByEmail(loginRequest.getEmail());
        if (player == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號不存在");

        if (!passwordEncoder.matches(loginRequest.getPassword(), player.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("密碼錯誤");
        }

        // ✅ 已經有 token 就重用
        String token = player.getToken();
        if (token == null || token.isBlank()) {
            token = UUID.randomUUID().toString();
            player.setToken(token);
            playerRepository.save(player);
        }

        return ResponseEntity.ok(token);
    }


    // 登出
    @PostMapping("/logout")
    public ResponseEntity<?> logoutPlayer(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body("請提供 token");
        }

        PlayerModel player = playerRepository.findByToken(token);
        if (player == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("無效的 token");
        }

        player.setToken(null);
        playerRepository.save(player);

        return ResponseEntity.ok("登出成功");
    }


    // userName 查詢玩家
    @GetMapping("/username/{userName}")
    public ResponseEntity<?> getPlayerByUserName(@PathVariable String userName) {
        PlayerModel player = playerRepository.findByUserName(userName);
        if (player != null) {
            return ResponseEntity.ok(player);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("查無此使用者名稱：" + userName);
        }
    }

    // 刪除玩家
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePlayerById(@PathVariable String id) {
        Optional<PlayerModel> player = playerRepository.findById(id);
        if (player.isPresent()) {
            playerRepository.deleteById(id);
            return ResponseEntity.ok("玩家已成功刪除，ID：" + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("查無此 ID：" + id);
        }
    }


    // 更新玩家資料
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable String id, @RequestBody PlayerModel updatedPlayer) {
        Optional<PlayerModel> existingPlayerOpt = playerRepository.findById(id);
        if (!existingPlayerOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("查無此 ID：" + id);
        }
        PlayerModel existingPlayer = existingPlayerOpt.get();

        if (updatedPlayer.getUserName() != null && !updatedPlayer.getUserName().isBlank()) {
            existingPlayer.setUserName(updatedPlayer.getUserName());
        }

        if (updatedPlayer.getEmail() != null && !updatedPlayer.getEmail().isBlank()) {
            PlayerModel playerWithEmail = playerRepository.findByEmail(updatedPlayer.getEmail());
            if (playerWithEmail != null && !playerWithEmail.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("此 email 已存在，請使用其他 email");
            }
            existingPlayer.setEmail(updatedPlayer.getEmail());
        }

        if (updatedPlayer.getPassword() != null && !updatedPlayer.getPassword().isBlank()) {
            existingPlayer.setPassword(passwordEncoder.encode(updatedPlayer.getPassword()));
        }

        PlayerModel savedPlayer = playerRepository.save(existingPlayer);
        return ResponseEntity.ok(savedPlayer);
    }
}