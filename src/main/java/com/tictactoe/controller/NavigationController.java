package com.tictactoe.controller;

/*
 * Problem No. #107
 * Difficulty: Easy
 * Description: Manages UI state transitions using CardLayout; updated for Registration flow.
 * Link: https://docs.oracle.com/javase/tutorial/uiswing/layout/card.html
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */


import javax.swing.*;
import java.awt.*;

import static com.tictactoe.controller.NavigationController.Screen.GAME;

public class NavigationController {

    public static class Screen {
        public static final String STARTUP = "STARTUP";
        public static final String LOGIN = "LOGIN";
        public static final String REGISTER = "REGISTER"; // NEW: Screen identifier
        public static final String GAME = "GAME";
        public static final String LOBBY = "LOBBY";
    }

    private final JPanel container;
    private final CardLayout cardLayout;

    public NavigationController(JPanel container) {
        if (!(container.getLayout() instanceof CardLayout)) {
            throw new IllegalArgumentException("Container must use CardLayout");
        }
        this.container = container;
        this.cardLayout = (CardLayout) container.getLayout();
    }

    private void navigateTo(String screenName) {
        if (SwingUtilities.isEventDispatchThread()) {
            cardLayout.show(container, screenName);
        } else {
            SwingUtilities.invokeLater(() -> cardLayout.show(container, screenName));
        }
    }

    public void showStartup() {
        navigateTo(Screen.STARTUP);
    }
    public void showLogin() {
        navigateTo(Screen.LOGIN);
    }

    // NEW: Method to trigger the Register form
    public void showRegister() {
        navigateTo(Screen.REGISTER);
    }

    public void showGame() {
        navigateTo(GAME);
    }
    public void showLobby() {
        navigateTo(Screen.LOBBY);
    }
}