package com.tictactoe.view;

/**
 * Problem No. #104
 * Difficulty: Intermediate
 * Description: UI Panel for the Mock Lobby displaying available players and their status
 * Link: https://github.com/VijayKumarCode/TicTacToe_Project
 * Time Complexity: O(n) to render the player list
 * Space Complexity: O(n) for the list model
 */


import com.tictactoe.controller.GameController;
import com.tictactoe.controller.NavigationController;
import com.tictactoe.model.Player;
import com.tictactoe.model.JSONHandler;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

public class LobbyPanel extends JPanel {
    private final DefaultListModel<Player> listModel;
    private final JList<Player> playerList;
    private final JButton requestButton;
    private JLabel welcomeLabel;

    public LobbyPanel(NavigationController nav, GameController gameController) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel header = new JLabel("Available Opponents");
        header.setFont(new Font("Ubuntu", Font.BOLD, 18));
        header.setAlignmentX(CENTER_ALIGNMENT);

        this.welcomeLabel = new JLabel("Welcome, Guest!");
        this.welcomeLabel.setFont(new Font("Ubuntu", Font.ITALIC, 14));
        this.welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);
        this.welcomeLabel.setForeground(Color.GRAY);

        topPanel.add(header);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(welcomeLabel);

        add(topPanel, BorderLayout.NORTH);

        this.listModel = new DefaultListModel<>();
        this.playerList = new JList<>(listModel);

        this.playerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value,
                                                                   int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Player p) {
                    label.setText(p.getName() + " (" + p.getStatus() + ")");
                    if ("Online".equals(p.getStatus())) {
                        label.setForeground(new Color(34, 139, 34));
                    }
                }
                return label;
            }
        });

        add(new JScrollPane(playerList), BorderLayout.CENTER);

        this.requestButton = new JButton("Send Match Request");
        this.requestButton.setBackground(new Color(70, 130, 180));
        this.requestButton.setForeground(Color.WHITE);

        this.requestButton.addActionListener(e -> {
            Player selected = playerList.getSelectedValue();
            if (selected != null) {
                String message = "Enter Address:\n" +
                        "â€¢ WiFi: 192.168.x.x\n" +
                        "â€¢ Internet: 0.tcp.in.ngrok.io:xxxxx\n" +
                        "(Leave blank to HOST)";

                String input = JOptionPane.showInputDialog(this, message);

                if (input != null) {
                    String targetIp = input.trim();
                    gameController.setOpponent(selected);
                    gameController.initiateNetworkMatch(targetIp);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an opponent first!");
            }
        });

        add(requestButton, BorderLayout.SOUTH);
        refreshLobby();
    }

    public void setWelcomeMessage(String username) {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + username + "!");
        }
    }

    public void refreshLobby() {
        if (listModel == null) return;

        listModel.clear();

        List<Player> players = JSONHandler.loadPlayers();
        for (Player p : players) {
            updateEntry(p.getName(), p.getType().name());
        }
    }

    public void updateEntry(String name, String typeStr) {
        for (int i = 0; i < listModel.getSize(); i++) {
            if (listModel.getElementAt(i).getName().equalsIgnoreCase(name)) {
                return;
            }
        }

        com.tictactoe.model.Player.PlayerType type;
        try {
            type = com.tictactoe.model.Player.PlayerType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            type = com.tictactoe.model.Player.PlayerType.ANONYMOUS;
        }

        // FIX: Match new constructor (name, email, password, type)
        Player newEntry = new Player(name, null, null, type);
        // Set symbol and status explicitly if needed, although defaults ("O", "Online") are likely fine here
        newEntry.setStatus("Online");

        listModel.addElement(newEntry);
    }
}