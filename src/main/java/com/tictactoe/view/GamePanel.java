package com.tictactoe.view;

/*
 * Problem No. #131
 * Difficulty: Intermediate
 * Description: UI with 'Difficulty' label hidden by default (Exclusive to Guest Mode).
 * Link: https://docs.oracle.com/javase/tutorial/uiswing/layout/border.html
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */

import com.tictactoe.controller.GameController;
import com.tictactoe.view.components.BoardPanel;
import com.tictactoe.view.components.ScoreBoardPanel;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameController controller;
    private final JLabel statusLabel;
    private final JLabel difficultyLabel; // The label in question
    private final BoardPanel boardPanel;
    private final ScoreBoardPanel scoreBoard;

    public GamePanel(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(48, 10, 36)); // Ubuntu Purple
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // --- NORTH: NAVIGATION ONLY ---
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        navBar.setOpaque(false);

        JButton homeButton = new JButton("Home");
        styleNavButton(homeButton);
        homeButton.addActionListener(e -> handleHomeRequest());

        JButton lobbyButton = new JButton("Lobby");
        styleNavButton(lobbyButton);
        lobbyButton.addActionListener(e -> handleLobbyRequest());

        navBar.add(homeButton);
        navBar.add(lobbyButton);

        // --- CENTER: BOARD ---
        this.boardPanel = new BoardPanel(controller);

        // --- SOUTH: FOOTER (Difficulty + Score + Status) ---
        JPanel footer = new JPanel(new BorderLayout(0, 5));
        footer.setOpaque(false);

        // 1. Difficulty Label -> HIDDEN BY DEFAULT [cite: 2026-01-25]
        difficultyLabel = new JLabel(" ", SwingConstants.CENTER);
        difficultyLabel.setFont(new Font("Ubuntu", Font.BOLD, 12));
        difficultyLabel.setVisible(false); // Default: Hidden (for Registered/WiFi)

        // 2. Scoreboard
        this.scoreBoard = new ScoreBoardPanel();

        // 3. Status Label
        statusLabel = new JLabel("Initializing...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Ubuntu", Font.BOLD, 16));
        statusLabel.setForeground(new Color(240, 119, 70)); // Ubuntu Orange

        footer.add(difficultyLabel, BorderLayout.NORTH);
        footer.add(scoreBoard, BorderLayout.CENTER);
        footer.add(statusLabel, BorderLayout.SOUTH);

        add(navBar, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    /**
     * Call this ONLY for Guest matches.
     * Passing null or empty string hides the label.
     */
    public void setDifficultyDisplay(String level) {
        if (level == null || level.trim().isEmpty()) {
            difficultyLabel.setVisible(false);
            return;
        }

        difficultyLabel.setVisible(true); // Reveal only for Guests
        difficultyLabel.setText("Difficulty: " + level);

        // Ubuntu-style color coding
        switch (level.toUpperCase()) {
            case "HARD" -> difficultyLabel.setForeground(new Color(231, 76, 60)); // Red
            case "MEDIUM" -> difficultyLabel.setForeground(new Color(241, 196, 15)); // Yellow
            default -> difficultyLabel.setForeground(new Color(46, 204, 113)); // Green
        }
    }

    public void updateStatus(String text) {
        statusLabel.setText(text);
    }

    // --- DELEGATION METHODS ---
    public void updateScore(String winnerType) {
        if ("USER".equals(winnerType)) scoreBoard.incrementUserWins();
        else if ("AI".equals(winnerType)) scoreBoard.incrementAIWins();
        else if ("DRAW".equalsIgnoreCase(winnerType)) scoreBoard.incrementDraws();
    }

    public void updateButton(int index, String symbol) {
        boardPanel.updateButton(index, symbol);
    }

    public void highlightWinningPath(int[] indices) {
        boardPanel.highlightWinningPath(indices);
    }

    public void clearBoard() {
        boardPanel.clearBoard();
        updateStatus("New Game Started");
    }

    private void styleNavButton(JButton btn) {
        btn.setBackground(new Color(119, 41, 83));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Ubuntu", Font.PLAIN, 14));
    }

    private void handleHomeRequest() {
        if (JOptionPane.showConfirmDialog(this, "Quit match?", "Confirm", 0) == 0)
            controller.handleHomeNavigation();
    }

    private void handleLobbyRequest() {
        if (JOptionPane.showConfirmDialog(this, "Return to lobby?", "Confirm", 0) == 0)
            controller.handleBackToLobby();
    }
}