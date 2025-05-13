package com.tkuimwd.api;

import java.util.concurrent.CompletableFuture;

import com.almasb.fxgl.dsl.FXGL;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.tkuimwd.api.dto.MatchData;
import com.tkuimwd.util.Fetch;

import javafx.application.Platform;

public class API {

    // login
    // return String token
    public static CompletableFuture<String> getLoginInfo(ObjectNode node) {
        return Fetch.login(node.toString())
                .thenApply(token -> {
                    System.out.println("登入成功");
                    return token;
                })
                .exceptionally(ex -> {
                    System.out.println("登入失敗");
                    ex.printStackTrace();
                    return "";
                });
    }

    // register
    public static CompletableFuture<Boolean> getRegisterInfo(ObjectNode node) {
        return Fetch.register(node.toString())
                .thenApply(responseBody -> {
                    System.out.println("註冊成功！");
                    return true;
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    Platform.runLater(() -> {
                        FXGL.getDialogService().showErrorBox("註冊失敗", () -> {
                        });
                    });
                    return false;
                });
    }

    // create room
    // return String roomCode
    public static CompletableFuture<String> getCreateRoomInfo(ObjectNode node) {
        ObjectMapper mapper = new ObjectMapper();
        return Fetch.createRoom(node.toString())
                .thenApply(response -> {
                    try {
                        JsonNode root = mapper.readTree(response);
                        String roomCode = root.get("roomCode").asText();
                        System.out.println("創建房間成功，房間代碼：" + roomCode);
                        return roomCode;
                    } catch (JsonProcessingException e) {
                        System.out.println("創建房間失敗，無法解析 JSON");
                        e.printStackTrace();
                        return null;
                    }
                })
                // 遇到例外就回傳 false
                .exceptionally(ex -> {
                    System.out.println("創建房間失敗");
                    ex.printStackTrace();
                    return null;
                });
    }

    // join room
    // return String info
    public static CompletableFuture<String> getJoinRoomInfo(ObjectNode node) {
        return Fetch.joinRoom(node.toString())
                .thenApply(response -> {
                    System.out.println("加入房間成功");
                    return response;
                })
                .exceptionally(ex -> {
                    System.out.println("加入房間失敗");
                    throw new RuntimeException("加入房間失敗", ex);
                });

    }

    // delete room
    // todo

    // get room guest token
    // return String hostToken
    public static CompletableFuture<String> getRoomGuestToken(String roomCode) {
        ObjectMapper mapper = new ObjectMapper();
        return Fetch.roomInfo(roomCode)
                .thenApply(response -> {
                    try {
                        JsonNode root = mapper.readTree(response);
                        JsonNode jt = root.get("guestToken");
                        String guestToken = jt == null ? null : jt.asText();
                        System.out.println("取得 guestToken: " + guestToken);
                        return guestToken;
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    public static CompletableFuture<String> getRoomHostToken(String roomCode) {
        ObjectMapper mapper = new ObjectMapper();
        return Fetch.roomInfo(roomCode)
                .thenApply(response -> {
                    try {
                        JsonNode root = mapper.readTree(response);
                        String hostToken = root.get("hostToken").asText();
                        System.out.println("取得HostToken成功：" + hostToken);
                        return hostToken;
                    } catch (JsonProcessingException e) {
                        System.out.println("取得HostToken失敗");
                        // e.printStackTrace();
                        return null;
                    }
                })
                .exceptionally(ex -> {
                    System.out.println("取得 HostToken 失敗");
                    throw new RuntimeException("取得HostToken失敗", ex);
                });
    }

    // get name
    public static CompletableFuture<String> getName(String token) {
        return Fetch.getName(token)
                .thenApply(response -> {
                    System.out.println("取得名稱成功：" + response);
                    return response;
                })
                .exceptionally(ex -> {
                    System.out.println("取得名稱失敗");
                    ex.printStackTrace();
                    return null;
                });
    }

    public static CompletableFuture<Boolean> setPlayerReady(ObjectNode node) {
        return Fetch.playerReady(node.toString())
                .thenApply(response -> {
                    System.out.println("取得準備狀態成功：" + response);
                    return true;
                })
                .exceptionally(ex -> {
                    System.out.println("取得準備狀態失敗");
                    ex.printStackTrace();
                    return false;
                });
    }

    public static CompletableFuture<Boolean> getPlayerReadyStatus(String roomCode) {
        ObjectMapper mapper = new ObjectMapper();
        return Fetch.roomInfo(roomCode)
                .thenApply(response -> {
                    try {
                        JsonNode root = mapper.readTree(response);
                        String status = root.get("guestStatus").asText();
                        System.out.println("guestStatus：" + status);
                        if (status.equals("準備")) {
                            return true;
                        } else {
                            return false;
                        }
                    } catch (JsonProcessingException e) {
                        System.out.println("取得guestStatus失敗");
                        // e.printStackTrace();
                        return null;
                    }
                })
                .exceptionally(ex -> {
                    System.out.println("取得 guestStatus 失敗");
                    throw new RuntimeException("取得guestStatus失敗", ex);
                });
    }

    public static CompletableFuture<MatchData> getStart(ObjectNode node){
        return Fetch.startGame(node.toString())
                .thenApply(response -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        MatchData matchData = mapper.readValue(response, MatchData.class);
                        System.out.println("取得開始資訊成功");
                        return matchData;
                    } catch (JsonProcessingException e) {
                        System.out.println("取得開始資訊失敗");
                        e.printStackTrace();
                        return null;
                    }
                })
                .exceptionally(ex -> {
                    System.out.println("取得開始資訊失敗");
                    ex.printStackTrace();
                    return null;
                });
    }

    public static CompletableFuture<MatchData> getMatchInfo(String roomId){
        return Fetch.getMatchInfoByRoomId(roomId)
                .thenApply(response -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        MatchData matchData = mapper.readValue(response, MatchData.class);
                        System.out.println("取得對局資訊成功:" + matchData.toString());
                        return matchData;
                    } catch (JsonProcessingException e) {
                        System.out.println("取得對局資訊失敗");
                        e.printStackTrace();
                        return null;
                    }
                })
                .exceptionally(ex -> {
                    System.out.println("取得對局資訊失敗");
                    ex.printStackTrace();
                    return null;
                });
    }

}
