A collection of small games (like Tic-Tac-Toe) implemented for fun, practice, and learning core programming concepts.

## Overview
This repo contains a set of simple interactive games organized under a `games` package. The goal is to practice logic, algorithms, and clean code structure through playable examples.

## Games Included

Tic-Tac-Toe | Classic 3x3 grid game vs CPU | Completed
Rock Paper Scissors | Classic Game vs CPU | Will add more detailed statistics to enhance game
Battle Game | Battle with attacks vs CPU | Completed, python implementation included for accuracy


## Project Structure
- `/games` – Package containing individual game classes.
- `/games/TicTacToe/` – Tic-Tac-Toe implementation
- `/games/RockPaperScissors/` - Rock Paper Scissors Implementation
- `/games/BattleGame/' - Custom Battle Game Implementation
- `README.md` – Project documentation.

## Getting Started

### Prerequisites
- Java (JDK 8+) or the language/runtime you are using.
- Git (optional but recommended).

### Installation
```bash
git clone https://github.com/Saahib-Singh/games.git
cd games
```


## How to Play
- Follow the on-screen prompts in the terminal.
- Input is usually:
  - Row and column numbers for grid-based games (like Tic-Tac-Toe).
  - Menu numbers for choosing options.
- Rules:
  - Tic-Tac-Toe: Players take turns placing marks on a 3x3 grid; first to get three in a row wins.

You can add separate subsections for each game with more detailed rules if needed.

## Adding a New Game
1. Create a new package or class under `src/games/` (for example, `games/connectfour`).
2. Implement the game logic and a `main` method to run it.
3. Ensure the game:
   - Compiles without errors.
   - Handles invalid input gracefully.

## Contributing
Contributions are welcome:
1. Fork this repository.
2. Create a feature branch.
3. Add or improve a game (code, tests, or documentation).
4. Open a pull request with a clear description of your changes.
