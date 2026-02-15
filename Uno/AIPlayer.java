//Imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an AI-controlled UNO player.
 * Provides simple decision logic to choose playable cards and colors.
 */
public class AIPlayer extends Player {

    /**
     * Constructs an AI player with the given name
     * @param name the AI player's name
     */
    public AIPlayer(String name) {
        super(name);
    }

    /**
     * Selects an index of a playable card in the AI's hand.
     * Strategy:
     * <ul>
     *     <li>Collect all playable cards.</li>
     *     <li>Prefer non-wild cards when possible.</li>
     *     <li>Fallback to the first playable card if all are wild.</li>
     * </ul>
     *
     * @param topCard      the current card on the discard pile
     * @param currentColor the active color in play
     * @return index of the chosen card, or -1 if no card is playable
     */
    public int chooseCardIndex(Card topCard, Card.Color currentColor) {
        List<Integer> playableIndexes = new ArrayList<>();

        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c.isPlayableOn(topCard, currentColor)) {
                playableIndexes.add(i);
            }
        }

        if (playableIndexes.isEmpty()) {
            return -1;
        }

        int bestIndex = -1;
        for (int idx : playableIndexes) {
            Card c = hand.get(idx);
            if (!c.isWild()) {
                bestIndex = idx;
                break;
            }
        }
        if (bestIndex == -1) {
            bestIndex = playableIndexes.get(0);
        }

        return bestIndex;
    }

    /**
     * Chooses a color when playing a wild card.
     * Heuristic: pick the color the AI has the most of in its hand.
     * @return the chosen color
     */
    public Card.Color chooseWildColor() {
        Map<Card.Color, Integer> counts = new HashMap<>();
        counts.put(Card.Color.RED, 0);
        counts.put(Card.Color.YELLOW, 0);
        counts.put(Card.Color.GREEN, 0);
        counts.put(Card.Color.BLUE, 0);

        for (Card c : hand) {
            if (c.getColor() != Card.Color.WILD) {
                counts.put(c.getColor(), counts.get(c.getColor()) + 1);
            }
        }

        Card.Color bestColor = Card.Color.RED;
        int bestCount = -1;
        for (Map.Entry<Card.Color, Integer> e : counts.entrySet()) {
            if (e.getValue() > bestCount) {
                bestCount = e.getValue();
                bestColor = e.getKey();
            }
        }

        if (bestCount == 0) {
            return Card.Color.RED;
        }
        return bestColor;
    }
}
