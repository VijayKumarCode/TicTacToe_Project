package com.tictactoe.model;

public enum GameState {
    // Phase 1: Pre-game
    WAITING_FOR_TOSS,      // Coin is flipping
    TOSS_WINNER_DECIDING,  // User won toss and is choosing Play or Pass

    // Phase 2: Active Play
    PLAYER_X_TURN,
    PLAYER_O_TURN,

    // Phase 3: Conclusion
    X_WON,
    O_WON,
    DRAW,

    // System State
    IDLE                   // Reset or main menu state
}
