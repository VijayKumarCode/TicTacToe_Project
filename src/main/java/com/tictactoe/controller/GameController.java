package com.tictactoe.controller;

/*
Problem No. #001
Difficulty: Medium
Description: TicTacToe_java - A modular Java implementation of Tic-Tac-Toe.
Link: https://github.com/VijayKumarCode/TicTacToe_java
Time Complexity: O(1)
Space Complexity: O(1)
*/

import com.tictactoe.model.Board;
import com.tictactoe.model.JSONHandler;
import com.tictactoe.model.Player;
import com.tictactoe.network.NetworkManager;
import com.tictactoe.view.GamePanel;
import com.tictactoe.view.LobbyPanel;
import com.tictactoe.view.UserLoginPanel;
import com.tictactoe.view.components.TossDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class GameController {
    // 1. Core Flags
    private boolean isGameActive = true;
    private boolean isAiProcessing = false;
    private String lastStatus = "";

    // 2. Network Fields
    private final NetworkManager networkManager = new NetworkManager();
    private boolean isMyTurn;
    private String mySymbol;

    // 3. Dependencies
    private final Board board;
    private final JFrame parentFrame;
    private final NavigationController nav;

    // 4. Game Entities
    private Player user;
    private Player ai;
    private Player opponent;
    private boolean isUserTurn;

    // 5. Configuration
    public enum Difficulty { EASY, MEDIUM, HARD }
    private Difficulty difficulty = Difficulty.EASY;

    // 6. View Components
    private GamePanel gamePanel;
    private UserLoginPanel loginPanel;
    private LobbyPanel lobbyPanel;
    private Timer aiTimer;

    public GameController(JFrame parentFrame, NavigationController nav) {
        this.board = new Board();
        this.parentFrame = parentFrame;
        this.nav = nav;

        // FIX: Match new constructor (name, email, password, type)
        // Guest has no email/password (null)
        this.user = new Player("Guest", null, null, Player.PlayerType.ANONYMOUS);
        this.user.setSymbol("X"); // Override default "O"

        this.ai = new Player("Ubuntu_Expert", null, null, Player.PlayerType.AI);
        // Default symbol is already "O" and status "Online"
    }

    public void initiateNetworkMatch(String ip) {
        if (gamePanel != null) gamePanel.setDifficultyDisplay(null);
        nav.showGame();
        if (ip == null || ip.trim().isEmpty()) startAsServer();
        else startAsClient(ip);
    }

    public void refreshStatus() {
        if (gamePanel == null) return;
        String text;

        if (networkManager.isActive()) {
            text = isMyTurn ? "Your Turn (" + mySymbol + ")" : "Waiting for Opponent...";
        } else {
            text = isUserTurn ? "Your Turn!" : "AI is thinking...";
        }

        if (!text.equals(lastStatus)) {
            updateStatus(text);
            lastStatus = text;
        }
    }

    public void startAsClient(String inputAddress) {
        String ip = (inputAddress != null && !inputAddress.isEmpty()) ? inputAddress : "127.0.0.1";
        updateStatus("Connecting to " + ip + "...");

        try {
            networkManager.connectToHost(ip, 5555, this::handleNetworkProtocol);
        } catch (IOException e) {
            showConnectionError("Connection failed.");
        }
    }

    public void startAsServer() {
        updateStatus("Waiting for opponent...");
        try {
            networkManager.startServer(5555, this::handleNetworkProtocol);
        } catch (IOException e) {
            showConnectionError("Port 5555 busy.");
        }
    }

    public void handleNetworkProtocol(String message) {
        SwingUtilities.invokeLater(() -> {
            if (message == null) return;

            if (message.startsWith("MOVE:")) {
                int index = Integer.parseInt(message.split(":")[1]);
                handleIncomingMove(index);
            } else if (message.equals("CONNECTED")) {
                updateStatus("Connected!");
                // ONLY the Host triggers the start flow for both
                if (networkManager.isHost()) {
                    startNewGameFlow();
                }
            } else if (message.startsWith("TOSS_RESULT:")) {
                boolean hostWon = Boolean.parseBoolean(message.split(":")[1]);
                // Client calculates their result based on the Host's roll
                boolean iWon = networkManager.isHost() ? hostWon : !hostWon;
                handleNetworkToss(iWon);
            } else if (message.equals("RESET_GAME")) {
                resetLocalBoardState();
            } else if (message.equals("CONNECTION_LOST")) {
                showConnectionError("Opponent disconnected.");
            }
        });
    }


    public void setDifficulty(String level) {
        try {
            this.difficulty = Difficulty.valueOf(level.toUpperCase());
            if (gamePanel != null) {
                gamePanel.setDifficultyDisplay(level);
            }
        } catch (IllegalArgumentException e) {
            this.difficulty = Difficulty.EASY;
        }
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
        this.ai = opponent;
        this.opponent.setStatus("In Game");
        System.out.println("Match initialized against: " + opponent.getName());
    }

    public void handleCellClick(int index) {
        if (!isGameActive || !board.getSymbolAt(index).isEmpty()) return;

        if (networkManager.isActive()) {
            if (!isMyTurn) return;

            executeMove(index, mySymbol);
            networkManager.sendMessage("MOVE:" + index);

            isMyTurn = false;
            refreshStatus();
        } else {
            if (!isUserTurn || isAiProcessing) return;
            executeMove(index, user.getSymbol());
        }
    }

    private void showConnectionError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
            handleBackToLobby();
        });
    }

    private void handleLogin(ActionEvent e) {
        String username = loginPanel.getUsername();
        char[] passwordChars = loginPanel.getPassword();
        String password = new String(passwordChars);

        if (JSONHandler.authenticate(username, password)){
            // FIX: Match new constructor (name, email, password, type)
            // We pass null for email here as we don't have it handy, or you can fetch it if needed.
            this.user = new Player(username, null, password, Player.PlayerType.REGISTERED);
            this.user.setSymbol("X"); // Set explicitly

            nav.showLobby();

            if (lobbyPanel != null) {
                lobbyPanel.setWelcomeMessage(username);
                lobbyPanel.refreshLobby();
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Invalid Username or Password");
        }
        java.util.Arrays.fill(passwordChars, '0');
    }


    public void handleGuestLogin() {
        stopNetwork();

        if (aiTimer != null) aiTimer.stop();
        this.isGameActive = true;
        this.isAiProcessing = false;

        String[] options = {"Easy", "Medium", "Hard"};
        int selection = JOptionPane.showOptionDialog(parentFrame,
                "Select AI Difficulty:", "Guest Match",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (selection == JOptionPane.CLOSED_OPTION) return;

        String chosenLevel = options[selection];
        this.difficulty = Difficulty.valueOf(chosenLevel.toUpperCase());

        // FIX: Match new constructor
        this.user = new Player("Guest", null, null, Player.PlayerType.ANONYMOUS);
        this.user.setStatus("Local");
        this.user.setSymbol(""); // Clear symbol for Toss

        this.ai = new Player("Ubuntu_Bot", null, null, Player.PlayerType.AI);
        this.ai.setStatus("Local");
        this.ai.setSymbol(""); // Clear symbol for Toss

        nav.showGame();

        SwingUtilities.invokeLater(() -> {
            if (gamePanel != null) {
                gamePanel.setDifficultyDisplay(chosenLevel);
                gamePanel.clearBoard();
            }
            board.resetBoard();
            startNewGameFlow();
        });
    }

    private void stopNetwork() {
        networkManager.stop();
    }

    public void startNewGameFlow() {
        resetLocalBoardState();

        if (networkManager.isActive()) {
            // NETWORK LOGIC: Host is the "Source of Truth"
            if (networkManager.isHost()) {
                networkManager.sendMessage("RESET_GAME");
                boolean hostWonToss = Math.random() < 0.5;
                networkManager.sendMessage("TOSS_RESULT:" + hostWonToss);
                handleNetworkToss(hostWonToss);
            }
            // Note: Client does nothing here; it waits for the TOSS_RESULT message.
        } else {
            // GUEST/AI LOGIC: Purely local
            boolean userWonToss = Math.random() < 0.5;
            if (userWonToss) handleUserTossWin();
            else handleAITossWin();

            if (!isUserTurn) triggerAIMove();
        }
    }
    private void resetLocalBoardState() {
        this.isAiProcessing = false;
        this.isGameActive = true;
        if (gamePanel != null) gamePanel.clearBoard();
        board.resetBoard();
    }

    private void handleNetworkToss(boolean iWon) {
        if (iWon) {
            this.mySymbol = "X";
            this.isMyTurn = true;
            this.user.setSymbol("X");
            TossDialog dialog = new TossDialog(parentFrame, "You");
            dialog.setVisible(true);
        } else {
            this.mySymbol = "O";
            this.isMyTurn = false;
            this.user.setSymbol("O");
            updateStatus("Opponent won Toss. Waiting...");
        }
        refreshStatus();
    }

    private void handleUserTossWin() {
        TossDialog dialog = new TossDialog(parentFrame, "You");
        dialog.setVisible(true);
        if (dialog.isPlayFirst()) {
            user.setSymbol("X"); ai.setSymbol("O"); isUserTurn = true;
        } else {
            user.setSymbol("O"); ai.setSymbol("X"); isUserTurn = false;
        }
    }

    private void handleAITossWin() {
        JOptionPane.showMessageDialog(parentFrame, "AI won Toss and plays X");
        ai.setSymbol("X"); user.setSymbol("O"); isUserTurn = false;
    }

    private void triggerAIMove() {
        if (isAiProcessing || isUserTurn || !isGameActive) return;

        isAiProcessing = true;
        refreshStatus();

        SwingWorker<Integer, Void> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() {
                try {
                    if (difficulty == null) return getRandomMove();

                    return switch (difficulty) {
                        case HARD -> getBestMoveMinimax();
                        case MEDIUM -> getSmartMove();
                        case EASY -> getRandomMove();
                        default -> getRandomMove();
                    };
                } catch (Exception e) {
                    System.err.println("Error in AI Background Thread: " + e.getMessage());
                    return getRandomMove();
                }
            }

            @Override
            protected void done() {
                try {
                    Integer moveIndex = get();
                    if (moveIndex != null && moveIndex >= 0 && moveIndex < 9) {
                        executeAIMove(moveIndex);
                    } else {
                        executeAIMove(getRandomMove());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isAiProcessing = false;
                    refreshStatus();
                }
            }
        };
        worker.execute();
    }

    private void executeAIMove(int index) {
        executeMove(index, getAiSymbol());
    }

    private int getRandomMove() {
        java.util.List<Integer> available = new java.util.ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (board.getSymbolAt(i).isEmpty()) {
                available.add(i);
            }
        }
        if (available.isEmpty()) return -1;
        return available.get(new java.util.Random().nextInt(available.size()));
    }

    private int getSmartMove() {
        for (int i = 0; i < 9; i++) {
            if (canMoveLeadToWin(i, getAiSymbol())) return i;
        }
        for (int i = 0; i < 9; i++) {
            if (canMoveLeadToWin(i, getUserSymbol())) return i;
        }
        return getRandomMove();
    }

    private int getBestMoveMinimax() {
        int bestScore = Integer.MIN_VALUE;
        int move = -1;

        for (int i = 0; i < 9; i++) {
            if (board.getSymbolAt(i).isEmpty()) {
                board.setSymbolAt(i, getAiSymbol());
                int score = minimax(board, 0, false);
                board.setSymbolAt(i, "");

                if (score > bestScore) {
                    bestScore = score;
                    move = i;
                }
            }
        }
        return move;
    }

    private int minimax(Board b, int depth, boolean isMaximizing) {
        if (b.getWinningIndices(getAiSymbol()) != null) return 10 - depth;
        if (b.getWinningIndices(getUserSymbol()) != null) return depth - 10;
        if (b.isFull()) return 0;

        int bestScore;
        if (isMaximizing) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (b.getSymbolAt(i).isEmpty()) {
                    b.setSymbolAt(i, getAiSymbol());
                    int score = minimax(b, depth + 1, false);
                    b.setSymbolAt(i, "");
                    bestScore = Math.max(score, bestScore);
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (b.getSymbolAt(i).isEmpty()) {
                    b.setSymbolAt(i, getUserSymbol());
                    int score = minimax(b, depth + 1, true);
                    b.setSymbolAt(i, "");
                    bestScore = Math.min(score, bestScore);
                }
            }
        }
        return bestScore;
    }

    private boolean canMoveLeadToWin(int index, String symbol) {
        if (!board.getSymbolAt(index).isEmpty()) return false;
        board.setSymbolAt(index, symbol);
        boolean wins = (board.getWinningIndices(symbol) != null);
        board.setSymbolAt(index, "");
        return wins;
    }

    private void announceWinner(Player winner) {
        isGameActive = false;
        String message = String.format("%s (%s) has won!", winner.getName(), winner.getSymbol());

        if (gamePanel != null) {
            gamePanel.updateScore(winner.getType().name());
        }

        JOptionPane.showMessageDialog(parentFrame, message, "Victory", JOptionPane.INFORMATION_MESSAGE);
        startNewGameFlow();
    }

    private void announceDraw() {
        if (gamePanel != null) {
            gamePanel.updateScore("DRAW");
        }
        JOptionPane.showMessageDialog(parentFrame, "It's a Draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        startNewGameFlow();
    }

    private boolean checkGameOver(String symbol) {
        int[] winningIndices = board.getWinningIndices(symbol);

        if (winningIndices != null) {
            if (gamePanel != null) {
                gamePanel.highlightWinningPath(winningIndices);
                gamePanel.updateStatus("Game Over!");
            }
            Player winner = (user.getSymbol().equals(symbol)) ? user : ai;
            announceWinner(winner);
            return true;
        }

        if (board.isFull()) {
            announceDraw();
            return true;
        }
        return false;
    }

    private void handleIncomingMove(int index) {
        SwingUtilities.invokeLater(() -> {
            if (mySymbol == null) return;
            String oppSym = mySymbol.equals("X") ? "O" : "X";
            executeMove(index, oppSym);
            this.isMyTurn = true;
            refreshStatus();
        });
    }

    private void executeMove(int index, String symbol) {
        int row = index / 3;
        int col = index % 3;

        if (board.makeMove(row, col, symbol)) {
            if (gamePanel != null) gamePanel.updateButton(index, symbol);
            if (checkGameOver(symbol)) return;

            if (!networkManager.isActive()) {
                if (symbol.equals(user.getSymbol())) {
                    isUserTurn = false;
                    refreshStatus();
                    aiTimer = new Timer(500, e -> triggerAIMove());
                    aiTimer.setRepeats(false);
                    aiTimer.start();
                } else {
                    isUserTurn = true;
                    refreshStatus();
                }
            }
        }
    }

    public void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> {
            if (gamePanel != null) gamePanel.updateStatus(status);
        });
    }

    public void setGamePanel(GamePanel p) {
        this.gamePanel = p;
    }

    public void setLoginPanel(UserLoginPanel p) {
        if (p != null) {
            this.loginPanel = p;
            p.addLoginListener(this::handleLogin);
        }
    }

    public void setLobbyPanel(LobbyPanel p) {
        this.lobbyPanel = p;
    }

    public void handleHomeNavigation() {
        stopNetwork();
        if (aiTimer != null && aiTimer.isRunning()) {
            aiTimer.stop();
        }
        nav.showStartup();
    }

    public void handleBackToLobby() {
        stopNetwork();
        nav.showLobby();
        if (lobbyPanel != null) lobbyPanel.refreshLobby();
    }

    public String getAiSymbol() {
        return ai.getSymbol();
    }
    public String getUserSymbol() {
        return user.getSymbol();
    }
}