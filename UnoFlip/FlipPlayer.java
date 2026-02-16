//Import
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a UNO Flip player (human or AI) with a hand of FlipCards.
 */
public class FlipPlayer {
    //Instance Variables
    protected final String name;
    protected final List<FlipCard> hand = new ArrayList<>();
    public FlipPlayer(String name) { this.name = name; } //Constructor
    public String getName() { return name;  }
    public List<FlipCard> getHand() { return hand; }

    /**
     * Draws one card from the given deck and adds it to the player's hand.
     * @param deck the FlipDeck to draw from
     */
    public void drawCard(FlipDeck deck) {
        FlipCard c = deck.draw();
        if (c != null) {
            hand.add(c);
        }
    }

    /**
     * Removes and returns the card at the given index in the hand.
     * @param index position of the card to play
     * @return the FlipCard removed from the hand
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public FlipCard playCard(int index) {
        return hand.remove(index);
    }

    /**
     * Checks whether the player has emptied their hand and therefore
     * won the current round.
     * @return true if the hand is empty, false otherwise
     */
    public boolean hasWonRound() {
        return hand.isEmpty();
    }

    /**
     * Returns the number of cards currently in the player's hand.
     * @return hand size
     */
    public int handSize() {
        return hand.size();
    }
}
