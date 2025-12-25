package rockpaperscissors;

/**
 * @author - Saahib M. Singh
 * Represents a single Rock Paper Scissors game session.
 * 
 * Each instance tracks the number of rounds won by the user,
 * the computer, and the number of ties. Class-level variables
 * track statistics across all game sessions.
 */
public class RockPaperScissors {

    // ---------- Class (static) variables ----------
    /** Total number of games played across all sessions. */
    private static int totalGamesPlayed = 0;

    /** Total number of games won by the user. */
    private static int totalUserGameWins = 0;

    /** Total number of games won by the computer. */
    private static int totalComputerGameWins = 0;

    /** Total number of tied games. */
    private static int totalGameTies = 0;

    // ---------- Instance variables ----------
    /** Number of rounds won by the user in this game session. */
    private int userRoundWins;

    /** Number of rounds won by the computer in this game session. */
    private int computerRoundWins;

    /** Number of rounds required to win the game. */
    private int roundsNeededToWin;

    /** Number of tied rounds in this game session. */
    private int ties;

    /**
     * Constructs a new Rock Paper Scissors game.
     *
     * @param roundsToWin the number of rounds needed to win the game
     */
    public RockPaperScissors(int roundsToWin) {
        userRoundWins = 0;
        computerRoundWins = 0;
        ties = 0;
        roundsNeededToWin = roundsToWin;
        totalGamesPlayed++;
    }

    // ---------- Class Methods ----------

    /**
     * Generates a random computer choice of "rock", "paper", or "scissors".
     * Uses the Randomizer class to generate a number between 0 and 2.
     *
     * @return the computer's choice as a lowercase string
     */
    public static String getRandomChoice() {
        int computerChoice = Randomizer.nextInt(0, 2);
        if (computerChoice == 0) {
        	return "rock";
        }
        
        if (computerChoice == 1) {
        	return "paper";
        }
        
        return "scissors";
    }

    /**
     * Prints overall statistics for all games played.
     */
    public static void printOverallStats() {
        System.out.println("Overall Game Statistics:");
        System.out.println("Total Games Played: " + totalGamesPlayed);
        System.out.println("User Game Wins: " + totalUserGameWins);
        System.out.println("Computer Game Wins: " + totalComputerGameWins);
        System.out.println("Ties: " + totalGameTies);
    }

    // ---------- Instance Methods ----------

    /**
     * Determines the winner of a single round based on classic rules:
     * <ul>
     *   <li>Rock beats Scissors</li>
     *   <li>Scissors beats Paper</li>
     *   <li>Paper beats Rock</li>
     * </ul>
     *
     * @param user the user's choice
     * @param computer the computer's choice
     * @return "user", "computer", or "tie"
     */
    public String determineRoundWinner(String user, String computer) {
        boolean userPaper = user.equalsIgnoreCase("paper");
        boolean computerPaper = computer.equalsIgnoreCase("paper");
        boolean userRock = user.equalsIgnoreCase("rock");
        boolean computerRock = computer.equalsIgnoreCase("rock");
        boolean userScissors = user.equalsIgnoreCase("scissors");
        boolean computerScissors = computer.equalsIgnoreCase("scissors");

        boolean userWon = (userPaper && computerRock)
                       || (userRock && computerScissors)
                       || (userScissors && computerPaper);

        boolean tie = user.equalsIgnoreCase(computer);
        boolean computerWon = !userWon && !tie;

        if (userWon) {
        	return "user";
        }
        
        if (computerWon) {
        	return "computer";
        }
        
        return "tie";
    }

    /**
     * Plays a single round of the game using the user's choice.
     * Generates a computer choice, determines the winner, updates
     * round statistics, and returns a message describing the outcome.
     *
     * @param userChoice the user's choice ("rock", "paper", or "scissors")
     * @return a message describing the round result
     */
    public String playRound(String userChoice) {
        String computerChoice = RockPaperScissors.getRandomChoice();
        String winner = determineRoundWinner(userChoice, computerChoice);

        if (winner.equals("user")) {
            userRoundWins++;
            return "User wins this round!";
        } else if (winner.equals("computer")) {
            computerRoundWins++;
            return "Computer wins this round!";
        } else {
            ties++;
            return "It's a tie!";
        }
    }

    /**
     * Checks whether either the user or the computer has won
     * enough rounds to win the game.
     *
     * @return true if the game has a winner, false otherwise
     */
    public boolean hasGameWinner() {
        return userRoundWins >= roundsNeededToWin
            || computerRoundWins >= roundsNeededToWin;
    }

    /**
     * Determines and returns a message announcing the winner of the game.
     * Updates global statistics accordingly.
     *
     * @return a message declaring the game winner
     */
    public String getGameWinnerMessage() {
        if (userRoundWins > computerRoundWins) {
            totalUserGameWins++;
            return "Congratulations! You won the game!";
        } else if (computerRoundWins > userRoundWins) {
            totalComputerGameWins++;
            return "Congratulations! Computer won the game!";
        } else {
            totalGameTies++;
            return "Oops. It's a tie...";
        }
    }

    /**
     * Prints the current score for this game session.
     */
    public void printCurrentGameScore() {
        System.out.println();
        System.out.print("Current Game Score - ");
        System.out.print(" User: " + userRoundWins);
        System.out.print(" | Computer: " + computerRoundWins);
        System.out.print(" | Ties: " + ties);
    }
}
