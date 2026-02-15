//Imports
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic UNO player (human or AI).
 * Stores the player's hand and supports basic hand operations.
 */
public class Player {
    protected final String name;
    protected final List<Card> hand = new ArrayList<>();
    public Player(String name) { this.name = name; } //Constructor
    public String getName() { return name; }
    public List<Card> getHand() { return hand; }

    /**
     * Draws a single card from the deck and adds it to the player's hand.
     * If the deck is empty, nothing is added
     * @param deck the deck to draw from
     */
    public void drawCard(Deck deck) {
        Card c = deck.draw();
        if (c != null) {
            hand.add(c);
        }
    }

    /**
     * Adds an already created card to the player's hand.
     * @param card the card to add
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Plays (removes) the card at the specified index from the hand.
     *
     * @param index the position in the hand to remove
     * @return the removed card
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Card playCard(int index) {
        return hand.remove(index);
    }

    /**
     * Returns true if this player has emptied their hand,
     * meaning they have won the current round.
     *
     * @return true if the player has no cards left
     */
    public boolean hasWonRound() {
        return hand.isEmpty();
    }

    /**
     * Returns how many cards the player currently holds.
     *
     * @return hand size
     */
    public int handSize() {
        return hand.size();
    }

    /**
     * Computes the sum of the points for all cards in the player's hand.
     * Used for scoring at the end of a round. [web:23]
     *
     * @return total point value of the hand
     */
    public int totalHandPoints() {
        int sum = 0;
        for (Card c : hand) {
            sum += Card.getPoints(c);
        }
        return sum;
    }
}
