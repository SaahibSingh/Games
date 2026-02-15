//Imports
import java.util.Comparator;
import java.util.List;

/**
 * Utility class for sorting UNO hands.
 * Sorts first by total points of each color group, then by individual card points.
 */
public class UnoSorter {

    /**
     * Sorts a hand in-place.
     * Grouping strategy:
     * <ul>
     *     <li>Compute the total points contributed by each color in the hand.</li>
     *     <li>Sort colors so that the colors with lower total points appear first.</li>
     *     <li>Within a color group, order cards by their individual point value.</li>
     * </ul>
     *
     * @param hand the list of cards to sort
     */
    public static void sortHand(List<Card> hand) {
        hand.sort(new Comparator<Card>() {
            @Override
            public int compare(Card a, Card b) {
                String colorA = Card.getColorKey(a);
                String colorB = Card.getColorKey(b);

                int totalA = getColorTotal(hand, colorA);
                int totalB = getColorTotal(hand, colorB);

                if (totalA != totalB) {
                    return Integer.compare(totalA, totalB);
                }

                int valueA = Card.getPoints(a);
                int valueB = Card.getPoints(b);
                return Integer.compare(valueA, valueB);
            }
        });
    }

    /**
     * Computes the total points of all cards in the hand that match the given color key.
     *
     * @param cards    the cards to examine
     * @param colorKey the color key ("RED", "BLUE", "WILD", etc.)
     * @return the sum of points for cards of that color
     */
    public static int getColorTotal(List<Card> cards, String colorKey) {
        return cards.stream()
                .filter(c -> Card.getColorKey(c).equals(colorKey))
                .mapToInt(Card::getPoints)
                .sum();
    }
}
