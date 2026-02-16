//Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the draw pile for UNO Flip.
 * Stores FlipCards and provides methods to reset, shuffle, and draw. [web:69][web:81]
 */
public class FlipDeck {
    private final List<FlipCard> cards = new ArrayList<>(); 
    public FlipDeck() { reset(); }

    /**
     * Clears and repopulates the deck with a simplified UNO Flip deck,
     * then shuffles it. 
     */
    public final void reset() {
        cards.clear();
        addNumberCards();
        addLightSpecials();
        addDarkSpecials();
        shuffle();
    }

    /**
     * Adds number card pairs (0–9) for each light/dark color pair.
     */
    private void addNumberCards() {
        FlipColor[] lightColors = {
                FlipColor.LIGHT_PINK,
                FlipColor.LIGHT_TEAL,
                FlipColor.LIGHT_PURPLE,
                FlipColor.LIGHT_ORANGE
        };

        FlipColor[] darkColors = {
                FlipColor.DARK_PINK,
                FlipColor.DARK_TEAL,
                FlipColor.DARK_PURPLE,
                FlipColor.DARK_ORANGE
        };

        for (int i = 0; i < lightColors.length; i++) {
            FlipColor lc = lightColors[i];
            FlipColor dc = darkColors[i];

            cards.add(new FlipCard(lc, FlipValue.ZERO, dc, FlipValue.ZERO));
            for (int j = 0; j < 2; j++) {
                cards.add(new FlipCard(lc, FlipValue.ONE, dc, FlipValue.ONE));
                cards.add(new FlipCard(lc, FlipValue.TWO, dc, FlipValue.TWO));
                cards.add(new FlipCard(lc, FlipValue.THREE, dc, FlipValue.THREE));
                cards.add(new FlipCard(lc, FlipValue.FOUR, dc, FlipValue.FOUR));
                cards.add(new FlipCard(lc, FlipValue.FIVE, dc, FlipValue.FIVE));
                cards.add(new FlipCard(lc, FlipValue.SIX, dc, FlipValue.SIX));
                cards.add(new FlipCard(lc, FlipValue.SEVEN, dc, FlipValue.SEVEN));
                cards.add(new FlipCard(lc, FlipValue.EIGHT, dc, FlipValue.EIGHT));
                cards.add(new FlipCard(lc, FlipValue.NINE, dc, FlipValue.NINE));
            }
        }
    }

    /**
     * Adds light-side special cards and their dark-side counterparts
     * (Skip, Reverse, Draw One/Five, Flip, Wild, etc.). [web:69][web:81]
     */
    private void addLightSpecials() {
        FlipColor[] lightColors = {
                FlipColor.LIGHT_PINK,
                FlipColor.LIGHT_TEAL,
                FlipColor.LIGHT_PURPLE,
                FlipColor.LIGHT_ORANGE
        };
      
        FlipColor[] darkColors = {
                FlipColor.DARK_PINK,
                FlipColor.DARK_TEAL,
                FlipColor.DARK_PURPLE,
                FlipColor.DARK_ORANGE
        };

        for (int i = 0; i < lightColors.length; i++) {
            FlipColor lc = lightColors[i];
            FlipColor dc = darkColors[i];

            cards.add(new FlipCard(lc, FlipValue.LIGHT_SKIP, dc, FlipValue.DARK_SKIP));
            cards.add(new FlipCard(lc, FlipValue.LIGHT_SKIP, dc, FlipValue.DARK_SKIP));

            cards.add(new FlipCard(lc, FlipValue.LIGHT_REVERSE, dc, FlipValue.DARK_REVERSE));
            cards.add(new FlipCard(lc, FlipValue.LIGHT_REVERSE, dc, FlipValue.DARK_REVERSE));

            cards.add(new FlipCard(lc, FlipValue.LIGHT_DRAW_ONE, dc, FlipValue.DARK_DRAW_FIVE));
            cards.add(new FlipCard(lc, FlipValue.LIGHT_DRAW_ONE, dc, FlipValue.DARK_DRAW_FIVE));

            cards.add(new FlipCard(lc, FlipValue.LIGHT_FLIP, dc, FlipValue.DARK_FLIP));
        }

        for (int i = 0; i < 4; i++) {
            cards.add(new FlipCard(
                    FlipColor.LIGHT_WILD, FlipValue.LIGHT_WILD,
                    FlipColor.DARK_WILD, FlipValue.DARK_WILD
            ));
          
            cards.add(new FlipCard(
                    FlipColor.LIGHT_WILD, FlipValue.LIGHT_WILD_DRAW_TWO,
                    FlipColor.DARK_WILD, FlipValue.DARK_WILD_DRAW_COLOR
            ));
        }
    }

    //randomly shuffles the deck of cards.
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Checks whether the deck is empty (no cards left to draw).
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Draws the top card from the deck, removing it from the list.
     * @return the drawn FlipCard, or null if the deck is empty
     */
    public FlipCard draw() {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }

    /**
     * Returns the number of cards currently in the deck.
     * @return the deck size
     */
    public int size() {
        return cards.size();
    }
}
