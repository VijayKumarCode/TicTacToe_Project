package com.tictactoe.model;

/**
 * Problem No. #104
 * Difficulty: Easy
 * Description: Model representing a player entity with type-based behavior
 * Link: N/A
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */


public class Player {
    // Enum to define types for easier logic handling
    public enum PlayerType {
        REGISTERED, ANONYMOUS, AI
    }

    private String name;
    private String symbol; // "X" or "O"
    private PlayerType type;
    private String status;

    // NEW: Recovery Fields
    private String email;
    private String password;

    // Updated Constructor
    public Player(String name, String email, String password, PlayerType type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
        this.symbol = "O"; // Default symbol for new players
        this.status = "Online";
    }

    // Getters and Setters for existing fields
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getName() {
        return name;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public PlayerType getType() {
        return type;
    }

    // NEW: Getters and Setters for Recovery
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return name + " (" + status + ")";
    }
}
