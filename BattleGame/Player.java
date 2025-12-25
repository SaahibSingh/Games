package battlegame;

//Imports
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a combatant in the Emoji Battle game, either the human player
 * or the AI opponent.
 * @author - Saahib M. Singh
 *
 * <p>A {@code Player} tracks:
 * <ul>
 *   <li>Current HP (health points)</li>
 *   <li>Whether the player is blocking the next attack</li>
 *   <li>Whether the player is poisoned</li>
 *   <li>How many times each attack has been used</li>
 * </ul>
 *
 * <p>The class provides methods for:
 * <ul>
 *   <li>Taking damage (with no internal blocking logic)</li>
 *   <li>Healing up to the maximum HP</li>
 *   <li>Applying poison damage at the end of a turn</li>
 *   <li>Checking and recording attack usage limits</li>
 * </ul>
 *
 * <p>This class contains no game‑flow logic; it simply models the state and
 * capabilities of a combatant.
 */

public class Player {

    private int hp = GameConstants.MAX_HP;
    private boolean blocked = false;
    private boolean poisoned = false;

    private final Map<String, Integer> usage = new HashMap<>();

    //Getters
    public int getHp() { return hp; }
    public boolean isBlocked() { return blocked; }
    public boolean isPoisoned() { return poisoned; }

    public void setBlocked(boolean b) { blocked = b; }
    public void setPoisoned(boolean p) { poisoned = p; }

    public void heal(int amount) {
        hp = Math.min(GameConstants.MAX_HP, hp + amount);
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    public void applyPoison() {
        if (poisoned) {
        	hp -= 5;
        }
    }

    public boolean canUse(Attack a) {
        return usage.getOrDefault(a.getName(), 0) < a.getMaxUses();
    }

    public void recordUse(Attack a) {
        usage.put(a.getName(), usage.getOrDefault(a.getName(), 0) + 1);
    }
}
