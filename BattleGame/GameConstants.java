package battlegame;
import java.util.*; //Import

/**
 * Provides all global constants used throughout the Emoji Battle game.
 * @author Saahib M. Singh
 * 
 * <p>This class centralizes:
 * <ul>
 *   <li>Emoji symbols for attacks and effects</li>
 *   <li>Attack names and ordering</li>
 *   <li>Damage values for each attack</li>
 *   <li>Animation frames for visual effects</li>
 *   <li>Maximum HP and usage limits</li>
 * </ul>
 *
 * <p>By storing all static game data in one place, the rest of the codebase
 * remains clean, maintainable, and easy to extend. Adding a new attack or
 * modifying damage values requires changes only in this class.
 *
 * <p>This class is not meant to be instantiated.
 */

public class GameConstants {
	//Variables
    public static final String DEFENDER = "🛡️";
    public static final String ATTACKER = "⚔️";
    public static final String FIREBALL = "🔥";
    public static final String ICE_SPEAR = "❄️";
    public static final String LIGHTNING = "⚡";
    public static final String HEALING = "🌿";
    public static final String EARTHQUAKE = "🌍";
    public static final String POISON = "☠️";
    public static final String ULTIMATE = "💥 Ultimate Blast";

    public static final int MAX_HP = 100;
    public static final int MAX_ATTACK_USE = 3;
    public static final int MAX_ULTIMATE_USE = 1;

    public static final List<String> ATTACK_ORDER = Arrays.asList(
            ATTACKER + " Sword Slash",
            FIREBALL + " Fireball",
            DEFENDER + " Shield Block",
            ICE_SPEAR + " Ice Spear",
            LIGHTNING + " Lightning Strike",
            HEALING + " Healing Winds",
            EARTHQUAKE + " Earthquake",
            POISON + " Poison Dart",
            ULTIMATE
    );

    public static final Map<String, Integer> DAMAGE = new HashMap<>();
    public static final Map<String, List<String>> ANIMATIONS = new HashMap<>();

    static {
        DAMAGE.put(ATTACKER + " Sword Slash", 15);
        DAMAGE.put(FIREBALL + " Fireball", 20);
        DAMAGE.put(DEFENDER + " Shield Block", 0);
        DAMAGE.put(ICE_SPEAR + " Ice Spear", 20);
        DAMAGE.put(LIGHTNING + " Lightning Strike", 20);
        DAMAGE.put(HEALING + " Healing Winds", -15);
        DAMAGE.put(EARTHQUAKE + " Earthquake", 15);
        DAMAGE.put(POISON + " Poison Dart", 15);
        DAMAGE.put(ULTIMATE, 40);

        ANIMATIONS.put(ATTACKER + " Sword Slash",
                Arrays.asList(" ", " =", " ==", " ===", " ====", " =====", "======="));

        ANIMATIONS.put(FIREBALL + " Fireball",
                Arrays.asList(" ( ) ", " (🔥) ", "(🔥🔥)", " (🔥) ", " ( ) "));

        ANIMATIONS.put(ICE_SPEAR + " Ice Spear",
                Arrays.asList("  ❄  ", "  ❄❄ ", " ❄❄❄ ", "  ❄❄ ", "   ❄  "));

        ANIMATIONS.put(LIGHTNING + " Lightning Strike",
                Arrays.asList("   ⚡   ", "  ⚡⚡  ", " ⚡⚡⚡ ", "  ⚡⚡  ", "   ⚡   "));

        ANIMATIONS.put(POISON + " Poison Dart",
                Arrays.asList("  ->   ", " -->   ", " --->  ", " --→   ", "  →    "));

        ANIMATIONS.put(ULTIMATE,
                Arrays.asList("     *      ", "    ***     ", "   *****    ",
                        "  *******   ", "   *****    ", "    ***     ", "     *      "));

        ANIMATIONS.put(HEALING + " Healing Winds",
                Arrays.asList("~", "~ 🌿 ~", "~ 🌿🌿 ~", "~ 🌿 ~", "~"));

        ANIMATIONS.put(DEFENDER + " Shield Block",
                Arrays.asList("[   ]", "[───]", "[███]", "[───]", "[   ]"));

        ANIMATIONS.put(EARTHQUAKE + " Earthquake",
                Arrays.asList("  🌍  ", " 🌍🌍 ", "🌍🌍🌍", " 🌍🌍 ", "  🌍  "));
    }
}
