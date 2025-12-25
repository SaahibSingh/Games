package battlegame;

import java.util.List; //Import

/**
 * Represents a single attack action in the Emoji Battle game.
 * @author - Saahib M. Singh
 * 
 * <p>An {@code Attack} encapsulates:
 * <ul>
 *   <li>The attack's display name</li>
 *   <li>Its base damage (positive, zero, or negative for healing)</li>
 *   <li>The maximum number of times it may be used</li>
 *   <li>The animation frames used when performing the attack</li>
 * </ul>
 *
 * <p>Attack objects are lightweight and immutable. They pull their data from
 * {@link GameConstants}, ensuring consistency across the game.
 *
 * <p>Examples:
 * <ul>
 *   <li>"🔥 Fireball" deals 20 damage</li>
 *   <li>"🛡️ Shield Block" deals 0 damage and enables blocking</li>
 *   <li>"🌿 Healing Winds" heals the user</li>
 * </ul>
 *
 * <p>Usage limits are enforced by the {@link Player} class, not by the attack itself.
 */


public class Attack {
	//Variables
    private final String name;
    private final int damage;
    private final int maxUses;
    private final List<String> animation;

    //Parameterized Constructor
    public Attack(String name) {
        this.name = name;
        this.damage = GameConstants.DAMAGE.get(name);
        this.animation = GameConstants.ANIMATIONS.get(name);
        this.maxUses = name.equals(GameConstants.ULTIMATE)
                ? GameConstants.MAX_ULTIMATE_USE
                : GameConstants.MAX_ATTACK_USE;
    }

    //Getters
    public String getName() { return name; }
    public int getDamage() { return damage; }
    public int getMaxUses() { return maxUses; }
    public List<String> getAnimation() { return animation; }
}
