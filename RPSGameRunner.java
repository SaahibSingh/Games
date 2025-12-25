package rockpaperscissors;

import java.util.Scanner; //*Import

/**
 * @author - Saahib M. Singh
 * Entry point for the Rock Paper Scissors game.
 * 
 * Handles user interaction, game setup, input validation,
 * and manages multiple game sessions.
 */
public class RPSGameRunner {

    /**
     * Main method that runs the Rock Paper Scissors program.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Loop for multiple game sessions
        while (true) {

            System.out.print("Enter number of rounds needed to win the game (or 0 to quit): ");
            int roundsToWin = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (roundsToWin == 0) {
            	break;
            }

            RockPaperScissors game = new RockPaperScissors(roundsToWin);

            // Play rounds until someone wins the game
            while (!game.hasGameWinner()) {
                String userChoice = "";

                // Validate user input
                while (true) {
                    System.out.print("\nEnter your choice (rock, paper, or scissors): ");
                    userChoice = scanner.nextLine().toLowerCase();

                    if (userChoice.equals("rock") ||
                        userChoice.equals("paper") ||
                        userChoice.equals("scissors")) {
                        break;
                    }

                    System.out.println("Invalid input. Please try again.");
                }

                // Play round and show result
                System.out.println(game.playRound(userChoice));
                game.printCurrentGameScore();
            }

            // Announce game winner
            System.out.println("\n" + game.getGameWinnerMessage());

            // Ask to play again
            System.out.print("\nDo you want to play another game? (yes/no): ");
            String playAgain = scanner.nextLine().toLowerCase();

            if (!playAgain.equals("yes")) {
            	break;
            }
        }

        scanner.close();

        // Print global statistics
        RockPaperScissors.printOverallStats();
        System.out.println("Thanks for playing!");
    }
}
