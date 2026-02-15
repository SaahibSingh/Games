//Import
import java.util.ArrayList;
import java.util.List;

public class Player { 
    //Instance Variables
    private final String name;
    private final List<Card> hand = new ArrayList<>();
    public Player(String name) { this.name = name; } //constructor

    //Getters
    public String getName() { return name; }
    public List<Card> getHand() { return hand; }

    /**
    Draws a card from a deck.
    @deck - the deck to draw the card from
    */
    public void drawCard(Deck deck) {
        Card c = deck.draw();
        if (c != null) {
            hand.add(c);
        }
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public Card playCard(int index) {
        return hand.remove(index);
    }

    public boolean hasWon() {
        return hand.isEmpty();
    }

    public int handSize() {
        return hand.size();
    }
}
