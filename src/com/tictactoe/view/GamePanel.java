
/**
 * Problem No. #105
 * Difficulty: Intermediate
 * Description: The 3x3 Grid UI for Tic Tac Toe
 * Link: N/A
 * Time Complexity: O(n) where n is number of cells (9)
 * Space Complexity: O(n) to store button references
 */

package com.tictactoe.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel {
    private JButton[] buttons = new JButton[9];

    public GamePanel() {
        // Industry Standard: Use GridLayout for a perfect square grid
        setLayout(new GridLayout(3, 3, 5, 5)); // 3x3 with 5px gaps
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initializeButtons();
    }

    private void initializeButtons() {
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Ubuntu", Font.BOLD, 50));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(Color.WHITE);
            add(buttons[i]);
        }
    }

    // This allows the Controller to assign logic to the buttons
    public void setButtonListener(int index, ActionListener listener) {
        buttons[index].addActionListener(listener);
    }

    public void updateButton(int index, String symbol) {
        buttons[index].setText(symbol);
        buttons[index].setEnabled(false); // Disable after click
    }

    public void clearBoard() {
        for (JButton btn : buttons) {
            btn.setText("");
            btn.setEnabled(true);
        }
    }
}