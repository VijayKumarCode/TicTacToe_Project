                        # 🎮 Tic-Tac-Toe: Ubuntu GUI Edition

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Style](https://img.shields.io/badge/Architecture-MVC-green)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
[![Platform](https://img.shields.io/badge/Platform-Ubuntu%20%2F%20Linux-E95420)](https://ubuntu.com/)

A modular, industry-standard Tic-Tac-Toe application built with **Java Swing**. This project showcases the transition from CLI logic to a Graphical User Interface (GUI), implementing advanced state management and loose coupling.

---

## 🚀 Project Overview

This application provides a professional gameplay flow designed to demonstrate robust user state handling:
* **Registered Mode:** Simulates a login flow and player matchmaking in a live lobby.
* **Guest Mode:** Allows instant play against a built-in **AI Agent**.

### 🪙 The "Toss Authority" Feature
Unlike traditional Tic-Tac-Toe games, this version introduces **Toss Authority**. The winner of the randomized pre-game toss is granted the power to decide the game's opening strategy:
1.  **Play First:** Assigns the player "X" and the first move.
2.  **Pass Turn:** Assigns the player "O" and allows the opponent (or AI) to open the game.

---

## 📂 Modular Architecture (MVC)

The project is decoupled into a strictly enforced **Model-View-Controller** pattern to ensure the logic remains independent of the UI—a standard practice in enterprise Java development.

| Package | Component | Responsibility |
| :--- | :--- | :--- |
| **Model** | `Board`, `Player`, `GameState` | Manages the 3x3 grid, win validation, and game lifecycle. |
| **View** | `MainFrame`, `GamePanel` | Handles rendering, Ubuntu-style layouts, and animations. |
| **Controller** | `GameController` | Acts as the bridge, processing inputs and updating the model. |

---

## 🛠️ Installation & Setup (Ubuntu/Linux)

### 1. Prerequisites
Ensure you have OpenJDK 17 installed:
```bash
sudo apt update
sudo apt install openjdk-17-jdk

2. Clone & Navigate

git clone [https://github.com/VijayKumarCode/TicTacToe_Project.git](https://github.com/VijayKumarCode/TicTacToe_Project.git)
cd TicTacToe_Project

3. Build & Run

Using the Terminal:

javac src/com/tictactoe/Main.java -d out
java -cp out com.tictactoe.Main

Using IntelliJ IDEA:

    File > Open > Select the project folder.

    Right-click src/com/tictactoe/Main.java and select Run.

📈 Roadmap

    [x] Initial MVC structure setup.

    [x] Implementation of "Toss Authority" logic.

    [ ] Next: Integration of Minimax AI Agent for "Unbeatable" mode.

    [ ] Local persistence for "Registered" user statistics using JSON/File I/O.

    [ ] Socket-based real-time multiplayer.

👤 Career Focus

I am an aspiring Remote Software Engineer specializing in Java and Open Source. This project serves as a practical application of my DSA studies and my goal to contribute to the Ubuntu/Canonical ecosystem.
