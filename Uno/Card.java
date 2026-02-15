import java.util.Objects; //Import

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

    //Parameterized Constructor
    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
    }

    //Getters
    public Color getColor() { return color; }
    public Value getValue() { return value; }

    /** Determines if the current card is a wild card **/
    public boolean isWild() {
        return value == Value.WILD || value == Value.WILD_DRAW_FOUR;
    }

    /**
    Determines if a card is playable
    @topCard - the current card 
    @currentColor - the current color of the round
    @return a boolean determining if topCard is playable
    **/
    public boolean isPlayableOn(Card topCard, Card.Color currentColor) {
        if (isWild()) return true;
        if (color == currentColor) return true;
        if (topCard == null) return true;
        return value == topCard.value;
    }

    //To String
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
