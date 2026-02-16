import java.util.Scanner; //Import
public class UnoFlipGame {
    private final FlipPlayer[] players = new FlipPlayer[2];
    private FlipDeck deck;
    private FlipSide side;
    private FlipCard topCard;
    private FlipColor currentColor;
    private int currentPlayerIndex;
    private boolean roundOver;
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new UnoFlipGame().start();
    }

    public void start() {
        System.out.println("=== UNO FLIP (Java, single round, 2 players) ===");
        System.out.println("Enter your name: ");
        players[0] = new FlipPlayer(scanner.nextLine());
        players[1] = new FlipAIPlayer("AI");
        setupNewRound();
        while (!roundOver) {
            takeTurn();
        }

        System.out.println("Game over!");
    }

    private void setupNewRound() {
        deck = new FlipDeck();
        side = FlipSide.LIGHT;
        for (FlipPlayer p : players) {
            p.getHand().clear();
        }

        for (int i = 0; i < 7; i++) {
            for (FlipPlayer p : players) {
                p.drawCard(deck);
            }
        }

        topCard = deck.draw();
        while (topCard == null || topCard.isWild(side) || topCard.isFlipCard(side)) {
            if (topCard != null) {
                // put aside specials as "burnt" for simplicity
            }
            topCard = deck.draw();
        }
        currentColor = topCard.getColor(side);
        currentPlayerIndex = 0;
        roundOver = false;

        System.out.println("\nStarting round on LIGHT side.");
        System.out.println("Starting card (LIGHT view): " +
                topCard.getColor(side) + " " + topCard.getValue(side));
    }

    private void takeTurn() {
        FlipPlayer current = players[currentPlayerIndex];
        System.out.println("\n--- " + current.getName() + "'s turn ---");
        System.out.println("Side: " + side);
        System.out.println("Top card: " +
                topCard.getColor(side) + " " + topCard.getValue(side));
        System.out.println("Current color: " + currentColor);
        System.out.println("Deck size: " + deck.size());

        if (current instanceof FlipAIPlayer) {
            handleAITurn((FlipAIPlayer) current);
        } else {
            handleHumanTurn(current);
        }

        if (current.hasWonRound()) {
            System.out.println(current.getName() + " goes out and wins the round!");
            roundOver = true;
        }
    }

    private void handleHumanTurn(FlipPlayer human) {
        while (true) {
            System.out.println("Your hand (showing " + side + " side):");
            for (int i = 0; i < human.getHand().size(); i++) {
                FlipCard c = human.getHand().get(i);
                System.out.println("[" + i + "] " +
                        c.getColor(side) + " " + c.getValue(side));
            }
            System.out.println("Enter card index to play, or -1 to draw:");

            int choice = readInt();
            if (choice == -1) {
                human.drawCard(deck);
                System.out.println("You drew a card.");
                advanceToNextPlayer();
                return;
            }
            if (choice < 0 || choice >= human.getHand().size()) {
                System.out.println("Invalid index.");
                continue;
            }

            FlipCard card = human.getHand().get(choice);
            if (!card.isPlayableOn(topCard, side, currentColor)) {
                System.out.println("You cannot play that card.");
                continue;
            }

            FlipCard played = human.playCard(choice);
            System.out.println("You played: " + played.getColor(side) +
                    " " + played.getValue(side));
            applyPlayedCard(played, human, true);
            return;
        }
    }

    private void handleAITurn(FlipAIPlayer ai) {
        int idx = ai.chooseCardIndex(topCard, side, currentColor);
        if (idx == -1) {
            ai.drawCard(deck);
            System.out.println("AI draws a card.");
            advanceToNextPlayer();
            return;
        }
        FlipCard played = ai.playCard(idx);
        System.out.println("AI plays: " + played.getColor(side) +
                " " + played.getValue(side));
        applyPlayedCard(played, ai, false);
    }

    private void applyPlayedCard(FlipCard card, FlipPlayer player, boolean isHuman) {
        topCard = card;
        currentColor = card.getColor(side);

        // Handle flip
        if (card.isFlipCard(side)) {
            System.out.println("FLIP! Switching sides and flipping all cards. [logical flip only]");
            side = (side == FlipSide.LIGHT) ? FlipSide.DARK : FlipSide.LIGHT;
            // In this model, we don't physically change cards; we just read the other side.
            // That's equivalent logically: every card now acts as its other face. [web:69][web:81]
        }

        // Handle wild color choice
        if (card.isWild(side)) {
            if (player instanceof FlipAIPlayer) {
                FlipColor chosen = ((FlipAIPlayer) player).chooseWildColor(side);
                System.out.println("AI chooses color: " + chosen);
                currentColor = chosen;
            } else {
                currentColor = promptForColor();
            }
        }

        // Very simplified action handling:
        // You can expand to implement Draw One, Draw Five, Skip, etc, mirroring our UNO logic.
        // For now, everything just passes turn except FLIP and WILD color change.
        advanceToNextPlayer();

        if (player.handSize() == 1) {
            System.out.println((isHuman ? "UNO!" : "AI says UNO!"));
        }
    }

    private void advanceToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
    }

    private FlipColor promptForColor() {
        System.out.println("Choose color index:");
        if (side == FlipSide.LIGHT) {
            System.out.println("0 = LIGHT_PINK, 1 = LIGHT_TEAL, 2 = LIGHT_PURPLE, 3 = LIGHT_ORANGE");
            while (true) {
                int c = readInt();
                switch (c) {
                    case 0: return FlipColor.LIGHT_PINK;
                    case 1: return FlipColor.LIGHT_TEAL;
                    case 2: return FlipColor.LIGHT_PURPLE;
                    case 3: return FlipColor.LIGHT_ORANGE;
                    default: System.out.println("Invalid color.");
                }
            }
        } else {
            System.out.println("0 = DARK_PINK, 1 = DARK_TEAL, 2 = DARK_PURPLE, 3 = DARK_ORANGE");
            while (true) {
                int c = readInt();
                switch (c) {
                    case 0: return FlipColor.DARK_PINK;
                    case 1: return FlipColor.DARK_TEAL;
                    case 2: return FlipColor.DARK_PURPLE;
                    case 3: return FlipColor.DARK_ORANGE;
                    default: System.out.println("Invalid color.");
                }
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
