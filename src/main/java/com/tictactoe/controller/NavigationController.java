package com.tictactoe.controller;

/**
 Problem No. #107
 * Difficulty: Easy
 * Description: Manages UI state transitions using CardLayout and string-based identifiers
 * Link: https://docs.oracle.com/javase/tutorial/uiswing/layout/card.html
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */


import javax.swing.*;
import java.awt.*;

public class NavigationController {
    public static final String STARTUP = "STARTUP";
    public static final String LOGIN = "LOGIN";
    public static final String GAME = "GAME";
    public static final String LOBBY = "LOBBY";

    private final JPanel container;
    private final CardLayout cardLayout;

    public NavigationController(JPanel container) {
        if (!(container.getLayout() instanceof CardLayout)) {
            throw new IllegalArgumentException("Container must use CardLayout");
        }

        this.container = container;
        this.cardLayout = (CardLayout) container.getLayout();
    }

    // Generic method to reduce repetition
    private void navigateTo(String screenName) {
        cardLayout.show(container, screenName);
    }

    public void showStartup() {
        navigateTo(STARTUP);
    }

    public void showLogin() {
        navigateTo(LOGIN);
    }

    public void showGame() {
        navigateTo(GAME);
    }

    public void showLobby() {
        navigateTo(LOBBY);
    }
}
