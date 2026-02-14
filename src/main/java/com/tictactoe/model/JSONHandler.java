package com.tictactoe.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSONHandler {
    private static final String FILE_PATH = "leaderboard.json";

    // --- New Logic for Registration & Recovery ---

    public static boolean registerNewUser(String username, String email, String password) {
        List<Player> players = loadPlayers();

        // Check if username or email already exists
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(username) || p.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }

        // Create new player using the updated (name, email, password, type) constructor
        Player newUser = new Player(username, email, password, Player.PlayerType.REGISTERED);
        players.add(newUser);
        savePlayers(players);
        return true;
    }

    public static String findUsernameByEmail(String email) {
        List<Player> players = loadPlayers();
        for (Player p : players) {
            if (p.getEmail() != null && p.getEmail().equalsIgnoreCase(email.trim())) {
                return p.getName();
            }
        }
        return null;
    }

    // --- Existing Methods ---

    public static List<Player> loadPlayers() {
        List<Player> players = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return players;

        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            content = content.replace("[", "").replace("]", "").replace("},", "}|");
            if (content.trim().isEmpty()) return players;

            String[] objects = content.split("\\|");
            for (String obj : objects) {
                String name = extractValue(obj, "name");
                String symbol = extractValue(obj, "symbol");
                String typeStr = extractValue(obj, "type");
                String status = extractValue(obj, "status");
                String email = extractValue(obj, "email");
                String pass = extractValue(obj, "password");

                Player.PlayerType type = Player.PlayerType.REGISTERED;
                try { type = Player.PlayerType.valueOf(typeStr); } catch (Exception e) {}

                Player p = new Player(name, email, pass, type);
                p.setSymbol(symbol);
                p.setStatus(status);
                players.add(p);
            }
        } catch (Exception e) { System.err.println("Parse error: " + e.getMessage()); }
        return players;
    }

    private static String extractValue(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = json.indexOf(pattern);
        if (start == -1) return "";
        start += pattern.length();
        int end = json.indexOf("\"", start);
        return (end == -1) ? "" : json.substring(start, end);
    }

    public static void savePlayers(List<Player> players) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            json.append(String.format(
                    "    {\"name\":\"%s\", \"symbol\":\"%s\", \"type\":\"%s\", \"status\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}",
                    p.getName(), p.getSymbol(), p.getType().name(), p.getStatus(),
                    p.getEmail() == null ? "" : p.getEmail(), p.getPassword() == null ? "" : p.getPassword()
            ));
            if (i < players.size() - 1) json.append(",\n");
        }
        json.append("\n]");
        try (Writer writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            writer.write(json.toString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static boolean authenticate(String username, String password) {
        List<Player> players = loadPlayers();
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(username) && p.getPassword() != null && p.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}