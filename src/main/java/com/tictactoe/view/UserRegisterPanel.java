package com.tictactoe.view;

/*
 * Problem No. #115
 * Difficulty: Intermediate
 * Description: Registration UI with validation and Ubuntu-themed styling.
 * Link: N/A
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */

import com.tictactoe.controller.NavigationController;
import com.tictactoe.model.JSONHandler;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.regex.Pattern;

public class UserRegisterPanel extends JPanel {
    private final JTextField usernameField = new JTextField(15);
    private final JTextField emailField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField confirmField = new JPasswordField(15);
    private final JButton registerButton = new JButton("Create Account");
    private final NavigationController nav;

    public UserRegisterPanel(NavigationController nav) {
        this.nav = nav;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Join the Game", SwingConstants.CENTER);
        title.setFont(new Font("Ubuntu", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        addFormRow("Username:", usernameField, 1, gbc);
        addFormRow("Email:", emailField, 2, gbc);
        addFormRow("Password:", passwordField, 3, gbc);
        addFormRow("Confirm:", confirmField, 4, gbc);

        registerButton.setBackground(new Color(119, 41, 83));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        add(registerButton, gbc);

        JButton backButton = new JButton("Back to Login");
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.GRAY);
        gbc.gridy = 6;
        add(backButton, gbc);

        backButton.addActionListener(e -> nav.showLogin());
        registerButton.addActionListener(e -> handleRegistration());
    }

    private void addFormRow(String labelText, JComponent component, int row, GridBagConstraints gbc) {
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        add(component, gbc);
    }

    private void handleRegistration() {
        String user = usernameField.getText().trim();
        String email = emailField.getText().trim();
        char[] passChars = passwordField.getPassword();
        char[] confirmChars = confirmField.getPassword();

        if (user.isEmpty() || email.isEmpty() || passChars.length == 0) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Arrays.equals(passChars, confirmChars)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            Arrays.fill(passChars, '0');
            Arrays.fill(confirmChars, '0');
            return;
        }

        String pass = new String(passChars);
        boolean success = JSONHandler.registerNewUser(user, email, pass);

        if (success) {
            JOptionPane.showMessageDialog(this, "Registration Successful! Please Login.");
            nav.showLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Username or Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        Arrays.fill(passChars, '0');
        Arrays.fill(confirmChars, '0');
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}