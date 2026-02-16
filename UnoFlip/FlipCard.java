/**
 * Represents a UNO Flip card, which has a light side and a dark side. 
 * Each side has its own color and value.
 */
public class FlipCard {
    //Instance Variables
    private final FlipColor lightColor;
    private final FlipValue lightValue;
    private final FlipColor darkColor;
    private final FlipValue darkValue;

    //Constructor
    public FlipCard(FlipColor lightColor, FlipValue lightValue,
                    FlipColor darkColor, FlipValue darkValue) {
        this.lightColor = lightColor;
        this.lightValue = lightValue;
        this.darkColor = darkColor;
        this.darkValue = darkValue;
    }

    /**
     * Returns this card's color on the specified side.
     * @param side the current side of play (LIGHT or DARK)
     * @return the color of this card on that side
     */
    public FlipColor getColor(FlipSide side) {
        return (side == FlipSide.LIGHT) ? lightColor : darkColor;
    }

    /**
     * Returns this card's value on the specified side.
     * @param side the current side of play (LIGHT or DARK)
     * @return the value of this card on that side
     */
    public FlipValue getValue(FlipSide side) {
        return (side == FlipSide.LIGHT) ? lightValue : darkValue;
    }

    /**
     * Checks whether this card is a wild-type card on the specified side
     * (e.g. LIGHT_WILD, LIGHT_WILD_DRAW_TWO, DARK_WILD, DARK_WILD_DRAW_COLOR).
     * @param side the side currently in play
     * @return true if this card is wild on that side, false otherwise
     */
    public boolean isWild(FlipSide side) {
        FlipValue v = getValue(side);
        return v == FlipValue.LIGHT_WILD ||
               v == FlipValue.LIGHT_WILD_DRAW_TWO ||
               v == FlipValue.DARK_WILD ||
               v == FlipValue.DARK_WILD_DRAW_COLOR;
    }

    /**
     * Checks whether this card is a FLIP card on the specified side.
     * @param side the side currently in play
     * @return true if this card is a flip card on that side, false otherwise
     */
    public boolean isFlipCard(FlipSide side) {
        FlipValue v = getValue(side);
        return v == FlipValue.LIGHT_FLIP || v == FlipValue.DARK_FLIP;
    }

    /**
     * Determines if this card is legally playable on top of the given top card
     * under UNO Flip color/value matching rules for the current side. 
     * @param top          the current top card on the discard pile (may be null at start)
     * @param side         the side currently in play
     * @param currentColor the active color that must be matched
     * @return true if this card can be played, false otherwise
     */
    public boolean isPlayableOn(FlipCard top,
                                FlipSide side,
                                FlipColor currentColor) {
        if (top == null) return true;
        if (isWild(side)) return true;

        FlipColor myColor = getColor(side);
        FlipValue myValue = getValue(side);
        FlipColor topColor = top.getColor(side);
        FlipValue topValue = top.getValue(side);

        return myColor == currentColor ||
               myColor == topColor ||
               myValue == topValue;
    }

    @Override
    public String toString() {
        return "[L:" + lightColor + " " + lightValue +
               " | D:" + darkColor + " " + darkValue + "]";
    }
}
