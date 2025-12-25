package battlegame;

import java.util.*; //Import from util package

/**
 * Handles all decision-making for the AI opponent.
 * @author - Saahib M. Singh
 *
 * <p>The AI uses a rule-based heuristic system to choose an attack each turn.
 * Its behavior is designed to feel reactive, strategic, and occasionally
 * unpredictable while remaining fair and readable.
 *
 * <p>The AI considers several factors:
 *
 * <h3>1. Survival Logic</h3>
 * If the AI's HP is below 30 and it still has uses of Healing Winds,
 * it has a 60% chance to heal instead of attacking.
 *
 * <h3>2. Finishing Pressure</h3>
 * If the player's HP is below 30, the AI prioritizes high-damage attacks
 * to attempt a finishing blow.
 *
 * <h3>3. Defensive Reaction</h3>
 * If the player's previous attack was strong (damage > 15),
 * the AI may choose Shield Block to reduce incoming damage.
 *
 * <h3>4. Fallback Behavior</h3>
 * If no special conditions apply, the AI selects randomly from all
 * remaining usable attacks.
 *
 * <p>The AI always respects attack usage limits and never selects an attack
 * that has been exhausted.
 *
 * @see Attack
 * @see Player
 */

public class AIController {

    private final Random random = new Random();

    /**
     * Selects the AI's attack for the current turn based on game state,
     * player behavior, and internal heuristics.
     *
     * <p>This method evaluates several tactical factors:
     *
     * <ul>
     *   <li><b>AI survival logic:</b>
     *       If the AI's HP is below 30 and it still has uses of Healing Winds,
     *       it has a 60% chance to heal instead of attacking.</li>
     *
     *   <li><b>Finishing pressure:</b>
     *       If the player's HP is below 30, the AI prioritizes high‑damage attacks
     *       (Fireball, Lightning Strike, Ice Spear, Earthquake, Poison Dart)
     *       as long as they are still available.</li>
     *
     *   <li><b>Defensive reaction:</b>
     *       If the player's previous attack was a strong move (damage > 15),
     *       the AI may choose Shield Block to reduce incoming damage.</li>
     *
     *   <li><b>Fallback behavior:</b>
     *       If no special conditions apply, the AI selects randomly from all
     *       remaining usable attacks.</li>
     * </ul>
     *
     * <p>The method ensures:
     * <ul>
     *   <li>Attacks respect their maximum allowed uses.</li>
     *   <li>Ultimate Blast is only chosen when allowed by usage limits.</li>
     *   <li>The AI never selects an attack it has exhausted.</li>
     * </ul>
     *
     * @param ai                The AI player object (tracks HP, status, usage).
     * @param player            The human player object.
     * @param lastPlayerAttack  The attack the player used last turn
     *                          (may be null on the first turn).
     *
     * @return The chosen {@link Attack} object representing the AI's move.
     */
    public Attack choose(Player ai, Player player, Attack lastPlayerAttack) {

        List<Attack> all = new ArrayList<>();
        for (String name : GameConstants.ATTACK_ORDER) {
            all.add(new Attack(name));
        }

        // Healing logic
        Attack heal = new Attack(GameConstants.HEALING + " Healing Winds");
        if (ai.getHp() < 30 && ai.canUse(heal) && random.nextDouble() < 0.6) {
            return heal;
        }

        // Weighted strong attacks
        if (player.getHp() < 30) {
            List<Attack> strong = Arrays.asList(
                    new Attack(GameConstants.LIGHTNING + " Lightning Strike"),
                    new Attack(GameConstants.FIREBALL + " Fireball"),
                    new Attack(GameConstants.ICE_SPEAR + " Ice Spear"),
                    new Attack(GameConstants.EARTHQUAKE + " Earthquake"),
                    new Attack(GameConstants.POISON + " Poison Dart")
            );

            List<Attack> usable = new ArrayList<>();
            for (Attack a : strong) {
            	if (ai.canUse(a)) {
            		usable.add(a);
            	}
            }

            if (!usable.isEmpty()) {
            	return usable.get(random.nextInt(usable.size()));
            }
        }

        // Shield reaction
        Attack shield = new Attack(GameConstants.DEFENDER + " Shield Block");
        if (lastPlayerAttack != null &&
                lastPlayerAttack.getDamage() > 15 &&
                ai.canUse(shield)) {
            return shield;
        }

        // Random fallback
        List<Attack> usable = new ArrayList<>();
        for (Attack a : all) {
        	if (ai.canUse(a)) {
        		usable.add(a);
        	}
        }

        return usable.get(random.nextInt(usable.size()));
    }
}
