package com.tictactoe.view.components;

/**
 * Problem No. #132
 * Difficulty: Easy
 * Description: Component to display and track Player vs AI scores.
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */


import javax.swing.*;
import java.awt.*;

public class ScoreBoardPanel extends JPanel {
    private final JLabel userScoreLabel;
    private final JLabel aiScoreLabel;
    private final JLabel drawScoreLabel;

    private int userWins = 0;
    private int aiWins = 0;
    private int draws = 0;

    public ScoreBoardPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        setOpaque(false);
        setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(119, 41, 83))); // Ubuntu Purple top border

        userScoreLabel = createScoreLabel("You: 0");
        drawScoreLabel = createScoreLabel("Draws: 0");
        aiScoreLabel = createScoreLabel("AI: 0");

        add(userScoreLabel);
        add(drawScoreLabel);
        add(aiScoreLabel);
    }

    private JLabel createScoreLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Ubuntu", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    public void incrementUserWins() {
        userWins++;
        userScoreLabel.setText("You: " + userWins);
    }

    public void incrementAIWins() {
        aiWins++;
        aiScoreLabel.setText("AI: " + aiWins);
    }

    public void incrementDraws() {
        draws++;
        drawScoreLabel.setText("Draws: " + draws);
    }
}
