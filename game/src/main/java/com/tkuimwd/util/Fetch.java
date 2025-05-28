package com.tkuimwd.util;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;


public class Fetch {

    private static final String BASE_URL = "http://192.168.1.26:8080";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    // 建立共用的 HttpClient
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    /**
     * 非同步 GET 請求
     * 
     * @param path API 路徑 (例如 "/player/login")
     * @return CompletableFuture<String> 以字串形式回傳完整 response body
     */
    public static CompletableFuture<String> get(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .timeout(TIMEOUT)
                .GET()
                .header("Accept", "application/json; charset=UTF-8")
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                .thenApply(response -> {
                    int code = response.statusCode();
                    if (code / 100 != 2) {
                        throw new RuntimeException("HTTP GET Error: " + code + " - " + response.body());
                    }
                    return response.body();
                });
    }

    /**
     * 非同步 POST 請求
     * 
     * @param path     API 路徑 (例如 "/player/register")
     * @param jsonBody 已經序列化好的 JSON 字串
     * @return CompletableFuture<String> 以字串形式回傳完整 response body
     */
    public static CompletableFuture<String> post(String path, String jsonBody) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .timeout(TIMEOUT)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Accept", "application/json; charset=UTF-8")
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                .thenApply(response -> {
                    int code = response.statusCode();
                    if (code / 100 != 2) {
                        throw new RuntimeException("HTTP POST Error: " + code + " - " + response.body());
                    }
                    return response.body();
                });
    }

    // API
    public static CompletableFuture<String> register(String jsonBody) {
        return post("/players/register", jsonBody);
    }

    public static CompletableFuture<String> login(String jsonBody) {
        return post("/players/login", jsonBody);
    }

    public static CompletableFuture<String> roomInfo(String params) {
        return get("/room/info?roomCode=" + params);
    }

    public static CompletableFuture<String> getName(String token) {
        return get("/players/token/" + token);
    }

    public static CompletableFuture<String> createRoom(String jsonBody) {
        return post("/room/create", jsonBody);
    }

    public static CompletableFuture<String> joinRoom(String jsonBody) {
        return post("/room/join", jsonBody);
    }

    public static CompletableFuture<String> playerReady(String jsonBody) {
        return post("/room/ready", jsonBody);
    }

    public static CompletableFuture<String> startGame(String jsonBody) {
        return post("/room/start", jsonBody);
    }

    public static CompletableFuture<String> getMatchInfoByRoomId (String id) {
        return get("/match/room/" + id);
    }

    public static CompletableFuture<String> getMatchInfoById (String id) {
        return get("/match/" + id);
    }

    
}