package battlegame;

/**
 * Tracks cumulative game statistics across multiple battles.
 *
 * <p>A {@code Stats} object records:
 * <ul>
 *   <li>Total number of player victories</li>
 *   <li>Total number of AI victories</li>
 *   <li>Total number of draws</li>
 *   <li>Total battles played</li>
 * </ul>
 *
 * <p>This class contains no game logic; it simply stores counters that are
 * updated by the {@link BattleEngine} and displayed by the UI.
 */

public class Stats {
	//Instance Variables
    public static int wins = 0;
    public static int losses = 0;
    public static int draws = 0;
    public static int battles = 0;
}
