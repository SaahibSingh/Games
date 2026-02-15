//Imports
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UnoGame {
    private final List<Player> players = new ArrayList<>();
    private final Deck deck = new Deck();
    private final List<Card> discardPile = new ArrayList<>();

    private int currentPlayerIndex = 0;
    private int direction = 1;
    private Card.Color currentColor;
    private Card topCard;
    private boolean gameOver = false;

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new UnoGame().start();
    }

    public void start() {
        System.out.println("=== UNO with Simple AI ===");
        System.out.println("Please enter your name");
        Player human = new Player(scanner.nextLine());
        players.add(human);

        AIPlayer ai = new AIPlayer("AI");
        players.add(ai);

        int initialCards = 7;
        for (Player p : players) {
            for (int i = 0; i < initialCards; i++) {
                p.drawCard(deck);
            }
        }

        topCard = deck.draw();
        while (topCard.isWild()) {
            discardPile.add(topCard);
            topCard = deck.draw();
        }
      
        discardPile.add(topCard);
        currentColor = topCard.getColor();

        System.out.println("Starting card: " + topCard + " | Current color: " + currentColor);

        while (!gameOver) {
            takeTurn();
        }

        System.out.println("Game over!");
    }

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

        if (current.hasWon()) {
            System.out.println(current.getName() + " wins!");
            gameOver = true;
            return;
        }

        if (!gameOver) {
            advanceToNextPlayer();
        }
    }

    private void handleHumanTurn(Player human) {
        while (true) {
            System.out.println("Your hand:");
            for (int i = 0; i < human.getHand().size(); i++) {
                System.out.println("[" + i + "] " + human.getHand().get(i));
            }
          
            System.out.println("Enter card index to play, or -1 to draw:");

            int choice = readInt();
            if (choice == -1) {
                human.drawCard(deck);
                System.out.println("You drew a card.");
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
            applyPlayedCard(played, human, true);
            return;
        }
    }

    private void handleAITurn(AIPlayer ai) {
        int index = ai.chooseCardIndex(topCard, currentColor);
        if (index == -1) {
            ai.drawCard(deck);
            System.out.println("AI draws a card.");
            return;
        }

        Card chosen = ai.playCard(index);
        System.out.println("AI plays: " + chosen);
        applyPlayedCard(chosen, ai, false);
    }

    private void applyPlayedCard(Card card, Player player, boolean isHuman) {
        discardPile.add(card);
        topCard = card;

        if (!card.isWild()) {
            currentColor = card.getColor();
        }

        if ((card.getValue() == Card.Value.WILD || card.getValue() == Card.Value.WILD_DRAW_FOUR)) {
            Card.Color chosenColor;
            if (player instanceof AIPlayer) {
                chosenColor = ((AIPlayer) player).chooseWildColor();
                System.out.println("AI chooses color: " + chosenColor);
            } else {
                chosenColor = promptForColor();
            }
            currentColor = chosenColor;
        }

        switch (card.getValue()) {
            case SKIP:
                System.out.println("Next player is skipped!");
                advanceToNextPlayer();
                break;
            case REVERSE:
                System.out.println("Direction reversed!");
                direction *= -1;
                if (players.size() == 2) {
                    advanceToNextPlayer();
                }
                break;
            case DRAW_TWO:
                System.out.println("Next player draws 2 cards and is skipped!");
                int nextIndex = getNextPlayerIndex();
                Player nextPlayer = players.get(nextIndex);
                nextPlayer.drawCard(deck);
                nextPlayer.drawCard(deck);
                currentPlayerIndex = nextIndex;
                advanceToNextPlayer();
                break;
            case WILD_DRAW_FOUR:
                System.out.println("Next player draws 4 cards and is skipped!");
                int idx = getNextPlayerIndex();
                Player victim = players.get(idx);
                for (int i = 0; i < 4; i++) {
                    victim.drawCard(deck);
                }
                currentPlayerIndex = idx;
                advanceToNextPlayer();
                break;
            default:
                break;
        }

        if (player.handSize() == 1) {
            System.out.println(isHuman ? "UNO!" : "AI says UNO!");
        }
    }

    private void advanceToNextPlayer() {
        currentPlayerIndex = getNextPlayerIndex();
    }

    private int getNextPlayerIndex() {
        int size = players.size();
        int next = (currentPlayerIndex + direction) % size;
        if (next < 0) next += size;
        return next;
    }

    private Card.Color promptForColor() {
        while (true) {
            System.out.println("Choose color: 0 = RED, 1 = YELLOW, 2 = GREEN, 3 = BLUE");
            int c = readInt();
            switch (c) {
                case 0: return Card.Color.RED;
                case 1: return Card.Color.YELLOW;
                case 2: return Card.Color.GREEN;
                case 3: return Card.Color.BLUE;
                default: System.out.println("Invalid color.");
            }
        }
    }

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
