//Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the draw pile in UNO.
 * Maintains the list of remaining cards to be drawn.
 */
public class Deck {
    private final List<Card> cards = new ArrayList<>();
    public Deck() { reset(); }

    /**
     * Clears and repopulates the deck with a full standard UNO deck,
     * then shuffles it. 
     */
    public final void reset() {
        cards.clear();

        // Add colored cards (0-9 + 2x each action card per color).
        for (Card.Color color : new Card.Color[]{Card.Color.RED, Card.Color.YELLOW,
                Card.Color.GREEN, Card.Color.BLUE}) {

            cards.add(new Card(color, Card.Value.ZERO));

            for (int i = 0; i < 2; i++) {
                cards.add(new Card(color, Card.Value.ONE));
                cards.add(new Card(color, Card.Value.TWO));
                cards.add(new Card(color, Card.Value.THREE));
                cards.add(new Card(color, Card.Value.FOUR));
                cards.add(new Card(color, Card.Value.FIVE));
                cards.add(new Card(color, Card.Value.SIX));
                cards.add(new Card(color, Card.Value.SEVEN));
                cards.add(new Card(color, Card.Value.EIGHT));
                cards.add(new Card(color, Card.Value.NINE));
                cards.add(new Card(color, Card.Value.SKIP));
                cards.add(new Card(color, Card.Value.REVERSE));
                cards.add(new Card(color, Card.Value.DRAW_TWO));
            }
        }

        // Add wilds.
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(Card.Color.WILD, Card.Value.WILD));
            cards.add(new Card(Card.Color.WILD, Card.Value.WILD_DRAW_FOUR));
        }

        shuffle();
    }

    /**
     * Randomly shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // @return true if the deck is empty, false otherwise
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Draws the top card from the deck, removing it from the deck.
     * @return the drawn card, or null if the deck is empty
     */
    public Card draw() {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }

    /**
     * Adds a card to the bottom of the deck (index 0).
     * @param card the card to be added
     */
    public void addToBottom(Card card) {
        cards.add(0, card);
    }

    /**
     * Returns how many cards remain in the deck.
     * @return the current deck size
     */
    public int size() {
        return cards.size();
    }
}
