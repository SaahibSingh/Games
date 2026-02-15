import java.util.Objects; //Import

/**
 * Represents a single UNO card with a color and value.
 * Includes helper methods for rules and scoring.
 */
public class Card {
    public enum Color {
        RED, YELLOW, GREEN, BLUE, WILD
    }

    public enum Value {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR
    }
    
    //Instance Variables
    private final Color color;
    private final Value value;

    //Constructor
    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
    }

    //Getters
    public Color getColor() { return color; }
    public Value getValue() { return value; }

    /**
     * Returns true if this card is any kind of Wild (Wild or Wild Draw Four).
     *
     * @return true if the card is wild, false otherwise
     */
    public boolean isWild() {
        return value == Value.WILD || value == Value.WILD_DRAW_FOUR;
    }

    /**
     * Determines whether this card can be legally played on top of the given
     * top card and current color.
     *
     * @param topCard      the current card on the discard pile (may be null at start)
     * @param currentColor the active color for the current turn
     * @return true if this card is playable, false otherwise
     */
    public boolean isPlayableOn(Card topCard, Card.Color currentColor) {
        if (isWild()) return true;
        if (color == currentColor) return true;
        if (topCard == null) return true;
        return value == topCard.value;
    }

    /**
     * Returns the official UNO points value of a card, used for scoring after a round. 
     * @param card the card whose points are being queried
     * @return the integer point value
     */
    public static int getPoints(Card card) {
        switch (card.getValue()) {
            case WILD:
            case WILD_DRAW_FOUR:
                return 50;
            case DRAW_TWO:
            case SKIP:
            case REVERSE:
                return 20;
            default:
                switch (card.getValue()) {
                    case ZERO: return 0;
                    case ONE: return 1;
                    case TWO: return 2;
                    case THREE: return 3;
                    case FOUR: return 4;
                    case FIVE: return 5;
                    case SIX: return 6;
                    case SEVEN: return 7;
                    case EIGHT: return 8;
                    case NINE: return 9;
                    default: return 0;
                }
        }
    }

    /**
     * Returns a string key representing the color grouping for sorting.
     * @param card the card whose color key is requested
     * @return "WILD" for wild cards, otherwise the color name (e.g., "RED")
     */
    public static String getColorKey(Card card) {
        if (card.getColor() == Color.WILD) return "WILD";
        return card.getColor().name();
    }

    @Override
    public String toString() {
        if (color == Color.WILD) {
            return value.toString();
        }
        return color + " " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return color == card.color && value == card.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, value);
    }
}
