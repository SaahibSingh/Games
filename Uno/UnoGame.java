//Imports
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main game controller for a console-based UNO game.
 * Supports:
 * <ul>
 *     <li>Human vs AI play.</li>
 *     <li>Choice between single-round mode and multi-round 500-point match.</li>
 *     <li>2-player special behavior for Reverse/Skip/Draw Two/Wild Draw Four.</li>
 * </ul>
 */
public class UnoGame {
    private final List<Player> players = new ArrayList<>();
    private Deck deck;
    private final List<Card> discardPile = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private int direction = 1;
    private Card.Color currentColor;
    private Card topCard;
    private boolean roundOver = false;
    private final Scanner scanner = new Scanner(System.in);
    private boolean pointsMode = false;
    private final int[] scores; // two players in this implementation
    public static void main(String[] args) {
        new UnoGame().start();
    }

    public UnoGame() {
        scores = new int[2];
    }

    /**
     * Starts the game:
     * <ol>
     *     <li>Prompts the user for game mode (single round vs 500-point match).</li>
     *     <li>Initializes players.</li>
     *     <li>Dispatches control to the appropriate loop.</li>
     * </ol>
     */
    public void start() {
        System.out.println("=== UNO with Simple AI ===");
        System.out.println("Please enter your name: ");
        Player human = new Player(scanner.nextLine());
        System.out.println("Choose mode:");
        System.out.println("1) Classic multi-round (first to 500 points)");
        System.out.println("2) Single-round (first to go out wins)");
        int choice = readModeChoice();
        pointsMode = (choice == 1);

        players.add(human);

        AIPlayer ai = new AIPlayer("AI");
        players.add(ai);

        if (pointsMode) {
            playPointsMatch();
        } else {
            playSingleRound();
        }

        System.out.println("Thanks for playing!");
    }

    /**
     * Reads and validates the mode choice (1 or 2) from the user.
     *
     * @return 1 for points match, 2 for single round
     */
    private int readModeChoice() {
        while (true) {
            int c = readInt();
            if (c == 1 || c == 2) return c;
            System.out.println("Enter 1 or 2:");
        }
    }

    /**
     * Sets up a new round:
     * <ul>
     *     <li>Creates and shuffles a fresh deck.</li>
     *     <li>Clears all hands and the discard pile.</li>
     *     <li>Deals 7 cards to each player.</li>
     *     <li>Flips a non-wild starting card.</li>
     *     <li>Resets turn order and flags.</li>
     * </ul>
     */
    private void setupNewRound() {
        deck = new Deck();
        discardPile.clear();
        for (Player p : players) {
            p.getHand().clear();
        }

        int initialCards = 7;
        for (Player p : players) {
            for (int i = 0; i < initialCards; i++) {
                p.drawCard(deck);
            }
        }

        // Flip the first non-wild card as the starting card. [web:23]
        topCard = deck.draw();
        while (topCard.isWild()) {
            discardPile.add(topCard);
            topCard = deck.draw();
        }
        discardPile.add(topCard);
        currentColor = topCard.getColor();

        currentPlayerIndex = 0;
        direction = 1;
        roundOver = false;

        System.out.println("\nNew round starting...");
        System.out.println("Starting card: " + topCard + " | Current color: " + currentColor);
    }

    /**
     * Plays a single round of UNO. The game ends when one player empties their hand.
     */
    private void playSingleRound() {
        setupNewRound();

        while (!roundOver) {
            takeTurn();
        }
    }

    /**
     * Plays a multi-round match where the first player to reach 500 points wins.
     * Points are earned according to official UNO scoring by capturing the
     * value of all other players' remaining cards. [web:23][web:55]
     */
    private void playPointsMatch() {
        boolean matchOver = false;

        while (!matchOver) {
            setupNewRound();

            while (!roundOver) {
                takeTurn();
            }

            int winnerIndex = findRoundWinnerIndex();
            if (winnerIndex == -1) {
                System.out.println("Round ended with no winner? (should not happen)");
                return;
            }

            int roundPoints = calculateRoundPointsForWinner(winnerIndex);
            scores[winnerIndex] += roundPoints;

            System.out.println(players.get(winnerIndex).getName() + " wins the round and earns " +
                    roundPoints + " points.");
            System.out.println("Scores: " +
                    players.get(0).getName() + "=" + scores[0] + " | " +
                    players.get(1).getName() + "=" + scores[1]);

            // Official match target is 500 points. [web:23][web:50]
            if (scores[winnerIndex] >= 500) {
                System.out.println(players.get(winnerIndex).getName() + " reaches 500 points and wins the match!");
                matchOver = true;
            } else {
                System.out.println("Press Enter to start the next round...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Locates the index of the player who has emptied their hand.
     *
     * @return index of the round winner, or -1 if none
     */
    private int findRoundWinnerIndex() {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getHand().isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Computes the total points earned by the round winner by summing
     * the point values of all cards remaining in opponents' hands. [web:23][web:51]
     *
     * @param winnerIndex index of the winning player
     * @return total points earned this round
     */
    private int calculateRoundPointsForWinner(int winnerIndex) {
        int sum = 0;
        for (int i = 0; i < players.size(); i++) {
            if (i == winnerIndex) continue;
            for (Card c : players.get(i).getHand()) {
                sum += Card.getPoints(c);
            }
        }
        return sum;
    }

    /**
     * Executes a single player's turn (either human or AI).
     * After the turn, checks if that player has won the round.
     */
    private void takeTurn() {
        Player current = players.get(currentPlayerIndex);
        System.out.println("\n--- " + current.getName() + "'s turn ---");
        System.out.println("Top card: " + topCard + " | Current color: " + currentColor);
        System.out.println("Deck size: " + deck.size());

        if (current instanceof AIPlayer) {
            handleAITurn((AIPlayer) current);
        } else {
            handleHumanTurn(current);
        }

        if (current.hasWonRound()) {
            System.out.println(current.getName() + " goes out!");
            roundOver = true;
        }
    }

    /**
     * Handles input and move selection for the human player:
     * <ul>
     *     <li>Sorts the hand for readability.</li>
     *     <li>Prompts for either drawing or choosing a card index.</li>
     *     <li>Validates plays against UNO rules.</li>
     * </ul>
     *
     * @param human the human player
     */
    private void handleHumanTurn(Player human) {
        while (true) {
            UnoSorter.sortHand(human.getHand());

            System.out.println("Your hand:");
            for (int i = 0; i < human.getHand().size(); i++) {
                System.out.println("[" + i + "] " + human.getHand().get(i));
            }
            System.out.println("Enter card index to play, or -1 to draw:");

            int choice = readInt();
            if (choice == -1) {
                human.drawCard(deck);
                System.out.println("You drew a card.");
                // Classic UNO: end turn after drawing (no auto-play). [web:23]
                advanceToNextPlayer();
                return;
            }
            if (choice < 0 || choice >= human.getHand().size()) {
                System.out.println("Invalid index.");
                continue;
            }

            Card chosen = human.getHand().get(choice);
            if (!chosen.isPlayableOn(topCard, currentColor)) {
                System.out.println("You cannot play that card.");
                continue;
            }

            Card played = human.playCard(choice);
            System.out.println("You played: " + played);
            applyPlayedCard(played, human, true);
            return;
        }
    }

    /**
     * Handles turn logic for the AI player:
     * <ul>
     *     <li>Sorts its hand (optional, mostly for debugging).</li>
     *     <li>Asks the AI for a playable card index.</li>
     *     <li>If none, draws; otherwise, plays the chosen card.</li>
     * </ul>
     *
     * @param ai the AI player
     */
    private void handleAITurn(AIPlayer ai) {
        UnoSorter.sortHand(ai.getHand());

        int index = ai.chooseCardIndex(topCard, currentColor);
        if (index == -1) {
            ai.drawCard(deck);
            System.out.println("AI draws a card.");
            advanceToNextPlayer();
            return;
        }

        Card chosen = ai.playCard(index);
        System.out.println("AI plays: " + chosen);
        applyPlayedCard(chosen, ai, false);
    }

    /**
     * Applies the effects of a played card (including special cards).
     * Implements the special 2-player behavior:
     * <ul>
     *     <li>Reverse acts like Skip – the same player plays again.</li>
     *     <li>Skip causes the opponent to lose their turn.</li>
     *     <li>Draw Two / Wild Draw Four cause the opponent to draw cards and lose their turn,
     *         with the current player going again.</li>
     * </ul>
     * For 3+ players, uses standard direction and skip rules. [web:23][web:35][web:42]
     *
     * @param card    the card that was played
     * @param player  the player who played the card
     * @param isHuman true if the player is the human user
     */
    private void applyPlayedCard(Card card, Player player, boolean isHuman) {
        discardPile.add(card);
        topCard = card;

        if (!card.isWild()) {
            currentColor = card.getColor();
        }

        // Handle wild color choice
        if (card.getValue() == Card.Value.WILD || card.getValue() == Card.Value.WILD_DRAW_FOUR) {
            Card.Color chosenColor;
            if (player instanceof AIPlayer) {
                chosenColor = ((AIPlayer) player).chooseWildColor();
                System.out.println("AI chooses color: " + chosenColor);
            } else {
                chosenColor = promptForColor();
            }
            currentColor = chosenColor;
        }

        int playerCount = players.size();

        switch (card.getValue()) {
            case SKIP:
                if (playerCount == 2) {
                    int skipped = getNextPlayerIndex();
                    System.out.println(players.get(skipped).getName() + " is skipped! You play again.");
                    // currentPlayerIndex remains on 'player'
                } else {
                    System.out.println("Next player is skipped!");
                    advanceToNextPlayer();
                }
                break;

            case REVERSE:
                if (playerCount == 2) {
                    int skipped = getNextPlayerIndex();
                    System.out.println("Reverse acts as Skip in 2-player. " +
                            players.get(skipped).getName() + " is skipped! You play again.");
                    // currentPlayerIndex remains on 'player'
                } else {
                    System.out.println("Direction reversed!");
                    direction *= -1;
                    advanceToNextPlayer();
                }
                break;

            case DRAW_TWO:
                int nextIndex = getNextPlayerIndex();
                Player nextPlayer = players.get(nextIndex);
                System.out.println(nextPlayer.getName() + " draws 2 cards and loses a turn!");

                nextPlayer.drawCard(deck);
                nextPlayer.drawCard(deck);

                if (playerCount == 2) {
                    // Same player goes again.
                } else {
                    currentPlayerIndex = nextIndex;
                    advanceToNextPlayer();
                }
                break;

            case WILD_DRAW_FOUR:
                int victimIndex = getNextPlayerIndex();
                Player victim = players.get(victimIndex);
                System.out.println(victim.getName() + " draws 4 cards and loses a turn!");

                for (int i = 0; i < 4; i++) {
                    victim.drawCard(deck);
                }

                if (playerCount == 2) {
                    // Same player goes again.
                } else {
                    currentPlayerIndex = victimIndex;
                    advanceToNextPlayer();
                }
                break;

            default:
                advanceToNextPlayer();
                break;
        }

        // UNO call
        if (player.handSize() == 1) {
            if (isHuman) {
                System.out.println("UNO!");
            } else {
                System.out.println("AI says UNO!");
            }
        }
    }

    /**
     * Advances the currentPlayerIndex according to the current direction.
     * Always moves exactly one step.
     */
    private void advanceToNextPlayer() {
        currentPlayerIndex = getNextPlayerIndex();
    }

    /**
     * Computes the index of the next player based on current index and direction,
     * wrapping around the players list as needed.
     *
     * @return index of the next player
     */
    private int getNextPlayerIndex() {
        int size = players.size();
        int next = (currentPlayerIndex + direction) % size;
        if (next < 0) next += size;
        return next;
    }

    /**
     * Prompts the human player to choose a color when playing a wild card.
     *
     * @return the chosen Card.Color
     */
    private Card.Color promptForColor() {
        while (true) {
            System.out.println("Choose color: 0=RED, 1=YELLOW, 2=GREEN, 3=BLUE");
            int c = readInt();
            switch (c) {
                case 0:
                    return Card.Color.RED;
                case 1:
                    return Card.Color.YELLOW;
                case 2:
                    return Card.Color.GREEN;
                case 3:
                    return Card.Color.BLUE;
                default:
                    System.out.println("Invalid color.");
            }
        }
    }

    /**
     * Reads an integer from standard input, reprompting until a valid integer is entered.
     *
     * @return the parsed integer
     */
    private int readInt() {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer:");
            }
        }
    }
}
