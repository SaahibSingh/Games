import java.util.*; //Import
public class UnoSorter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of cards: ");
        int n = sc.nextInt();
        sc.nextLine(); // Consume newline
        
        List<String> cards = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter card " + (i+1) + " (e.g., 'red 3', 'wild draw 4'): ");
            cards.add(sc.nextLine().trim().toLowerCase());
        }
        
        // Sort: first by color total points asc, then stable by individual value asc
        cards.sort((a, b) -> {
            String colorA = getColor(a);
            String colorB = getColor(b);
            int totalA = getColorTotal(cards, colorA);
            int totalB = getColorTotal(cards, colorB);
            if (totalA != totalB) return Integer.compare(totalA, totalB);
            return Integer.compare(getValue(a), getValue(b));
        });
        
        System.out.println("\nSorted hand (lowest color total points first, then by value):");
        for (String card : cards)  System.out.println(card);
        sc.close();
    }
    
    static int getValue(String card) {
        if (card.contains("draw 4") || card.contains("wild draw 4")) return 50;
        if (card.contains("+2") || card.contains("draw 2") || card.contains("skip") || card.contains("reverse") || card.contains("wild")) return 20;
        String[] parts = card.split(" ");
        if (parts.length >= 2 && parts[1].matches("\\d+")) return Integer.parseInt(parts[1]);
        return 0; 
    }
    
    static String getColor(String card) {
        if (card.contains("wild") || card.contains("draw 4")) return "Wild";
        String[] parts = card.split(" ");
        if (parts.length > 0) {
            String first = parts[0].substring(0, 1).toUpperCase();
            if ("RGBY".contains(first)) return first;
        }
        return "Wild";
    }
    
    static int getColorTotal(List<String> cards, String color) {
        return cards.stream()
                    .filter(c -> getColor(c).equals(color))
                    .mapToInt(UnoSorter::getValue)
                    .sum();
    }
}
