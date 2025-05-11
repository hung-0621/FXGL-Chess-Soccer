package com.tkuimwd.util;

import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fetch {

    private static final String BASE_URL = "http://localhost:8080/api";

    public static String get(String path) throws IOException {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            return in.readLine();
        }
    }

    public static String post(String path, String jsonBody) throws IOException {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonBody.getBytes("utf-8"));
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            return in.readLine();
        }
    }

    // API
    public static String register(String jsonBody) throws IOException {
        return post("/player/register", jsonBody);
    }

    public static String login(String jsonBody) throws IOException {
        return post("/player/login", jsonBody);
    }

    public static String createRoom(String jsonBody) throws IOException {
        return post("/game/create", jsonBody);
    }

    public static String joinRoom(String jsonBody) throws IOException {
        return post("/game/join", jsonBody);
    }

    public static String playerReady(String jsonBody) throws IOException {
        return post("/room/ready", jsonBody);
    }

    public static String startGame(String jsonBody) throws IOException {
        return post("/room/start", jsonBody);
    }
}
