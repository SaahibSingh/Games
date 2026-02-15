//Imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AIPlayer extends Player {
    public AIPlayer(String name) {  super(name); } //Constuctor

    /**
    Chooses the index from the best possible card based on the current color
    @topCard - current card
    @currentColor - current color
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
            // No colored cards, just pick red.
            return Card.Color.RED;
        }
      
        return bestColor;
    }
}
