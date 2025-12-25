package rockpaperscissors;

/**
 * @author - Saahib M. Singh
 * Utility class that provides simple random number generation
 * for the Rock Paper Scissors game.
 * 
 * This class wraps Math.random() to generate integers within
 * specific ranges. All methods are static, so no instances of
 * this class are required.
 */
public class Randomizer {

    /**
     * Generates a random integer between 1 and 10 (inclusive).
     *
     * @return a random integer from 1 to 10
     */
    public static int nextInt() {
        return (int)(Math.random() * 10 + 1);
    }

    /**
     * Generates a random integer between the given minimum and maximum values.
     *
     * @param min the smallest possible value
     * @param max the largest possible value
     * @return a random integer in the range [min, max]
     */
    public static int nextInt(int min, int max) {
        int range = max - min;
        return (int)(Math.random() * (range + 1) + min);
    }
}
