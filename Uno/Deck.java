//Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>(); //Instance Variable - ArrayList
    public Deck() { reset(); } //Constructor
    public final void reset() {
        cards.clear();
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

        for (int i = 0; i < 4; i++) {
            cards.add(new Card(Card.Color.WILD, Card.Value.WILD));
            cards.add(new Card(Card.Color.WILD, Card.Value.WILD_DRAW_FOUR));
        }

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card draw() {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }

    public void addToBottom(Card card) {
        cards.add(0, card);
    }

    public int size() {
        return cards.size();
    }
}
