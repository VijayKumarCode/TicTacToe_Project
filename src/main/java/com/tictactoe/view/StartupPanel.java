package com.tictactoe.view;

/*
 * Problem No. #102
 * Difficulty: Easy
 * Description: Refined Startup Panel with grouped Auth actions and Account Recovery
 * Link: N/A
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */

import com.tictactoe.controller.GameController;
import com.tictactoe.controller.NavigationController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StartupPanel extends JPanel {
    private final NavigationController nav;
    private final GameController gameController;
    private final Color UBUNTU_PURPLE = new Color(119, 41, 83); //
    private final Color BG_COLOR = new Color(245, 245, 245);    //

    public StartupPanel(NavigationController nav, GameController gameController) {
        this.nav = nav;
        this.gameController = gameController;

        setLayout(new GridBagLayout());
        setBackground(BG_COLOR);
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        // 1. App Title
        gbc.gridy = 0;
        add(createTitleLabel(), gbc);

        // 2. Main Action Panel (Login & Guest)
        gbc.gridy = 1;
        add(createMainActionPanel(), gbc);

        // 3. Registration Section
        gbc.gridy = 2;
        add(createRegistrationButton(), gbc);

        // 4. Recovery Section
        gbc.gridy = 3;
        add(createRecoveryPanel(), gbc);
    }

    private JLabel createTitleLabel() {
        JLabel title = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER);
        title.setFont(new Font("Ubuntu", Font.BOLD, 32));
        title.setForeground(UBUNTU_PURPLE);
        return title;
    }

    private JPanel createMainActionPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 10));
        panel.setOpaque(false);

        JButton btnLogin = createStyledButton("Login", true);
        btnLogin.addActionListener(e -> nav.showLogin());

        JButton btnGuest = createStyledButton("Play as Guest", false);
        btnGuest.addActionListener(e -> gameController.handleGuestLogin());

        panel.add(btnLogin);
        panel.add(btnGuest);
        return panel;
    }

    private JButton createRegistrationButton() {
        JButton btnRegister = new JButton("<html>Don't have an account? <b>Register Now</b></html>");
        styleLinkButton(btnRegister);
        btnRegister.addActionListener(e -> nav.showRegister());
        return btnRegister;
    }

    private JPanel createRecoveryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);

        JButton btnForgetPass = new JButton("Forget Password?");
        btnForgetPass.addActionListener(e -> handlePasswordRecovery());

        JButton btnForgetUser = new JButton("Forget Username?");
        btnForgetUser.addActionListener(e -> handleUsernameRecovery());

        styleLinkButton(btnForgetPass);
        styleLinkButton(btnForgetUser);

        panel.add(btnForgetPass);
        panel.add(btnForgetUser);
        return panel;
    }

// --- Logic Handlers ---

    private void handlePasswordRecovery() {
        String email = JOptionPane.showInputDialog(this, "Enter registered email for Password Reset:");
        if (email == null || email.trim().isEmpty()) return;

        String username = com.tictactoe.model.JSONHandler.findUsernameByEmail(email);
        if (username != null) {
            JOptionPane.showMessageDialog(this, "Reset link (simulated) sent to: " + email);
            System.out.println("LOG: Password reset for: " + username);
        } else {
            showError("No account found with that email.");
        }
    }

    private void handleUsernameRecovery() {
        String email = JOptionPane.showInputDialog(this, "Enter registered email to recover Username:");
        if (email == null || email.trim().isEmpty()) return;

        String foundUser = com.tictactoe.model.JSONHandler.findUsernameByEmail(email);
        if (foundUser != null) {
            JOptionPane.showMessageDialog(this, "Account Found! Your username is: " + foundUser);
        } else {
            showError("No account found with that email.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JButton createStyledButton(String text, boolean primary) {
        JButton button = new JButton(text);
        button.setFont(new Font("Ubuntu", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 40));

        if (primary) {
            button.setBackground(UBUNTU_PURPLE); //
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(UBUNTU_PURPLE);
            button.setBorder(BorderFactory.createLineBorder(UBUNTU_PURPLE, 2));
        }
        return button;
    }

    private void styleLinkButton(JButton button) {
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Ubuntu", Font.PLAIN, 12));
        button.setForeground(Color.DARK_GRAY);
    }
}