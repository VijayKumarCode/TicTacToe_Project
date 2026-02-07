# 🎮 Tic-Tac-Toe: Ubuntu GUI Edition

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Style](https://img.shields.io/badge/Architecture-MVC-green)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
[![Platform](https://img.shields.io/badge/Platform-Ubuntu%20%2F%20Linux-E95420)](https://ubuntu.com/)

A modular, industry-standard Tic-Tac-Toe application built with **Java Swing**. This project showcases the transition from CLI logic to a Graphical User Interface (GUI), implementing advanced state management and loose coupling.

---

## 🚀 Project Overview

This application provides a professional gameplay flow designed to demonstrate robust user state handling and UI navigation:
* **Registered Mode:** Captures user credentials via a `UserLoginPanel`, preserves the username, and transitions to the game board.
* **Guest Mode:** Allows instant "one-click" play against the **AI Agent** using an anonymous session.

### 🪙 The "Toss Authority" Feature
The winner of the randomized pre-game toss is granted the power to decide the opening strategy via a custom `TossDialog`:
1.  **Play First:** Assigns the player "X" and the first move.
2.  **Pass Turn:** Assigns the player "O" and allows the AI to open the game.

---

## 📂 Project Structure

```text
TicTacToe_Project/
├── .gitignore
├── README.md
└── src/
    └── com/
        └── tictactoe/
            ├── Main.java                 # Entry Point
            ├── controller/
            │   ├── GameController.java   # Logic & Events
            │   └── NavigationController.java # CardLayout
            ├── model/
            │   ├── Board.java            # Grid Logic
            │   └── Player.java           # Player Data
            └── view/
                ├── MainFrame.java        # Main Window
                ├── StartupPanel.java     # Landing Screen
                ├── GamePanel.java        # Board UI
                ├── UserLoginPanel.java   # Login UI
                └── components/
                    └── TossDialog.java   # Toss Modal
                    
                    🏗️ Modular Architecture (MVC)
                    Package,Component,Responsibility
Package,Component,Responsibility
Model,"Board, Player",Manages the 3x3 grid logic and player identity persistence.
View,"MainFrame, GamePanel",Handles the visual layout and user input events.
View.components,TossDialog,Specialized JDialog for pre-game decisions.
Controller,GameController,Orchestrates screen transitions and bridges UI with Logic.

🛠️ Installation & Setup (Ubuntu/Linux)
1. Prerequisites

Ensure you have OpenJDK 17 installed:
sudo apt update
sudo apt install openjdk-17-jdk

2. Build & Run
# Compile
javac -d out src/com/tictactoe/Main.java

# Run
java -cp out com.tictactoe.Main

📈 Roadmap

    [x] Initial MVC structure and package organization.

    [x] Implementation of "Toss Authority" decision logic.

    [x] Session persistence for Registered vs. Guest users.

    [ ] Next: Integration of Minimax AI Agent for an "Unbeatable" difficulty mode.

    [ ] Future: JSON-based local storage for player win/loss statistics.

    👤 Career Focus

I am an aspiring Remote Software Engineer specializing in Java and Open Source development. This project serves as a practical application of my DSA studies and my commitment to writing clean, maintainable code that aligns with the Ubuntu/Canonical ecosystem.
