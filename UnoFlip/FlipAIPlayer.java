/**
 * Simple AI player for UNO Flip.
 * Chooses a playable card index using a basic heuristic. 
 */
public class FlipAIPlayer extends FlipPlayer {
    public FlipAIPlayer(String name) { super(name); } //Constructor

    /**
     * Chooses an index of a card in the AI's hand that is playable
     * on the current top card and color for the given side.
     * Prefers non-wild cards when possible.
     *
     * @param top          the top card on the discard pile (may be null at start)
     * @param side         the side currently in play (LIGHT or DARK)
     * @param currentColor the active color that must be matched
     * @return index of a playable card, or -1 if no card can be played
     */
    public int chooseCardIndex(FlipCard top,
                               FlipSide side,
                               FlipColor currentColor) {
        int bestIndex = -1;
        for (int i = 0; i < hand.size(); i++) {
            FlipCard c = hand.get(i);
            if (c.isPlayableOn(top, side, currentColor)) {
                if (!c.isWild(side)) {
                    return i;
                } else if (bestIndex == -1) {
                    bestIndex = i;
                }
            }
        }
        return bestIndex;
    }

    /**
     * Chooses a color when the AI plays a wild card.
     * Current implementation picks a fixed favorite color per side.
     * @param side the side currently in play
     * @return the chosen FlipColor
     */
    public FlipColor chooseWildColor(FlipSide side) {
        return (side == FlipSide.LIGHT)
                ? FlipColor.LIGHT_PINK
                : FlipColor.DARK_PINK;
    }
}
