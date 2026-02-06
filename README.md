                            # 🎮 Tic-Tac-Toe: Ubuntu GUI Edition

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Style](https://img.shields.io/badge/Architecture-MVC-green)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)

A modular, industry-standard Tic-Tac-Toe application built with **Java Swing**. This project showcases the transition from CLI logic to a Graphical User Interface (GUI), implementing advanced state management and loose coupling.



---

## 🚀 Project Overview

This application provides two distinct gameplay flows designed to demonstrate user state handling:
* **Registered Mode:** Simulates a login flow and player matchmaking in a live lobby.
* **Guest Mode:** Allows instant play against a built-in **AI Agent**.

### 🪙 The "Toss Authority" Feature
Unlike traditional Tic-Tac-Toe games, this version introduces **Toss Authority**. The winner of the randomized pre-game toss is granted the power to decide the game's opening strategy:
1.  **Play First:** Assigns the player "X" and the first move.
2.  **Pass Turn:** Assigns the player "O" and allows the opponent (or AI) to open the game.

---

## 📂 Modular Architecture

The project follows the **Model-View-Controller (MVC)** pattern to ensure the logic is independent of the UI.

| Package | Component | Responsibility |
| :--- | :--- | :--- |
| **Model** | `Board`, `Player`, `GameState` | Manages the 3x3 grid, win validation, and game lifecycle. |
| **View** | `MainFrame`, `GamePanel`, `StartupPanel` | Handles rendering, layouts, and button animations. |
| **Controller** | `GameController` | Acts as the bridge, processing inputs and updating the model. |

### Directory Tree
```text
src/com/tictactoe/
├── Main.java              # Entry point
├── model/                 # Logic & Data
│   ├── Board.java         
│   ├── Player.java        
│   └── GameState.java     
├── view/                  # UI Components
│   ├── MainFrame.java     
│   ├── StartupPanel.java  
│   └── GamePanel.java     
└── controller/            # The "Brain"
    └── GameController.java

🛠️ Installation & Setup

    1.Prerequisites: Ensure you have JDK 17 or higher installed.

    2.Clone the Repository:
      git clone [https://github.com/your-username/TicTacToe_Ubuntu_Project.git](https://github.com/your-username/TicTacToe_Ubuntu_Project.git)
    3.Open in IntelliJ IDEA:

    File > Open > Select the project folder.

    4.Run:

      Right-click src/com/tictactoe/Main.java and select Run.
      
    📈 Roadmap & Contributions

    [x] Initial MVC structure setup.

    [x] Implementation of "Toss Authority" logic.

    [ ] Integration of Minimax AI Agent for "Unbeatable" mode.

    [ ] Local persistence for "Registered" user statistics.

    [ ] Socket-based real-time multiplayer.