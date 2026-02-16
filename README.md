Here’s an updated `README.md` you can paste over your current one and tweak as you like:

```markdown
# Games

A collection of small interactive games (like Tic-Tac-Toe, Rock Paper Scissors, and Uno) built for fun, practice, and learning core programming concepts. [page:1]

---

## Overview

This repository contains a set of terminal-based games implemented in Java and Python, organized into separate folders by game. [page:1]  
Each game focuses on improving **logic**, algorithms, and clean code structure through simple, playable examples. [page:1]

---

## Games Included

| Game              | Description                                        | Status                                     |
|-------------------|----------------------------------------------------|--------------------------------------------|
| Tic-Tac-Toe       | Classic 3x3 grid game vs CPU.                     | Completed. [page:1]                        |
| Rock Paper Scissors | Classic hand game vs CPU with planned stats.    | Core game implemented; stats planned. [page:1] |
| Battle Game       | Custom battle system with attacks vs CPU.        | Completed (Python implementation). [page:1] |
| Uno               | Terminal-based Uno card game.                    | Initial implementation added. [page:1]     |
| Uno Flip          | Implementation of UNO Flip with light/dark sides.| Initial implementation added. [page:1]     |

---

## Project Structure

```text
/
├─ BattleGame/        # Battle game (Python)
├─ RockPaperScissors/ # Rock Paper Scissors (Java)
├─ TicTacToe/         # Tic-Tac-Toe (Java)
├─ Uno/               # Uno game
├─ UnoFlip/           # Uno Flip game
└─ README.md          # Project documentation
```

Folder names map directly to individual games so you can jump into any game’s code quickly. [page:1]

---

## Getting Started

### Prerequisites

- Java (JDK 8+) for Java-based games. [page:1]  
- Python 3 for BattleGame and any other Python-based games. [page:1]  
- Git (optional but recommended for cloning the repo). [page:1]

### Installation

```bash
git clone https://github.com/SaahibSingh/Games.git
cd Games
```

---

## How to Play

Each game is run individually from its own folder. [page:1]

General behavior:

- Follow the on-screen prompts in the terminal. [page:1]
- Input is usually:
  - Row and column numbers for grid-based games (like Tic-Tac-Toe).
  - Menu numbers or text options for choosing moves or actions.
- Games handle basic invalid input and reprompt when possible. [page:1]

You can extend this section with game-specific instructions (for example, how special Uno/Uno Flip cards work).

### Example: Running Tic-Tac-Toe (Java)

1. Navigate to the Tic-Tac-Toe folder:
   ```bash
   cd TicTacToe
   ```
2. Compile:
   ```bash
   javac TicTacToeRunner.java
   ```
3. Run:
   ```bash
   java TicTacToeRunner
   ```

(Adjust class names if needed based on your actual files.)

---

## Adding a New Game

1. Create a new folder at the repo root (for example, `ConnectFour/`). 
2. Implement the game logic and a `main` entry point (Java `public static void main` or a Python `if __name__ == "__main__"` block).
3. Make sure the game:
   - Compiles or runs without errors.
   - Handles invalid input gracefully.
   - Prints clear instructions to the player.

---

## Contributing

Contributions are welcome:

1. Fork this repository. [page:1]  
2. Create a feature branch.  
3. Add or improve a game (code, tests, or documentation). [page:1]  
4. Open a pull request with a clear description of your changes. [page:1]

---

## License

This project is currently unlicensed.  
If you plan to reuse or extend the code publicly, consider adding an open-source license (for example, MIT or Apache 2.0).
```
