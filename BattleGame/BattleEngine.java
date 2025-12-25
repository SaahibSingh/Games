package battlegame;

import java.util.*; 

/**
 *  Controls the flow of a single battle between the player and the AI.
 * Handles both turn-based and health-based modes, attack resolution,
 * animations, and win/loss logic.
 *
 * @author - Saahib M. Singh
 * <p>The {@code BattleEngine} is responsible for:
 * <ul>
 *   <li>Running both game modes (turn-based and health-based)</li>
 *   <li>Executing attack actions and applying their effects</li>
 *   <li>Handling blocking, poison, healing, and damage resolution</li>
 *   <li>Animating attacks using ASCII/emoji frames</li>
 *   <li>Determining win/loss/draw outcomes</li>
 * </ul>
 *
 * <p>This class does not store persistent stats or menu logic; it focuses
 * solely on the mechanics of a single battle instance.
 *
 * <h3>Turn Execution</h3>
 * Each turn consists of:
 * <ol>
 *   <li>Player chooses an attack</li>
 *   <li>AI chooses an attack via {@link AIController}</li>
 *   <li>Player attack resolves</li>
 *   <li>AI attack resolves</li>
 *   <li>Poison damage is applied</li>
 * </ol>
 *
 * <h3>Damage Resolution</h3>
 * <ul>
 *   <li>Blocking halves incoming damage</li>
 *   <li>Healing attacks restore HP</li>
 *   <li>Poison deals 5 damage at end of turn</li>
 *   <li>Usage limits are enforced by {@link Player}</li>
 * </ul>
 *
 * <p>The engine is designed to be deterministic except for:
 * <ul>
 *   <li>AI decision randomness</li>
 *   <li>Poison application chance</li>
 *   <li>Animation timing</li>
 * </ul>
 */

public class BattleEngine {
	//Variables
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();
    private final AIController aiController = new AIController();

    public void runTurnsMode(Stats stats) {
        Player player = new Player();
        Player ai = new Player();
        Attack lastPlayerAttack = null;

        for (int turn = 1; turn <= 5; turn++) {
            System.out.println("\n--- Turn " + turn + " ---");
            showHealth(player, ai);

            Attack pAtk = choosePlayerAttack(player);
            Attack aAtk = aiController.choose(ai, player, lastPlayerAttack);

            executeTurn(player, ai, pAtk, aAtk);
            lastPlayerAttack = pAtk;

            if (checkEnd(stats, player, ai)) {
            	return;
            }
        }

        System.out.println("\nTime's up!");
        showHealth(player, ai);
        if (player.getHp() > ai.getHp()) {
            System.out.println("You win!");
            Stats.wins++;
        } else if (player.getHp() < ai.getHp()) {
            System.out.println("You lose!");
            Stats.losses++;
        } else {
            System.out.println("It's a draw!");
            Stats.draws++;
        }
        Stats.battles++;
    }

    public void runHealthMode(Stats stats) {
        Player player = new Player();
        Player ai = new Player();
        Attack lastPlayerAttack = null;

        while (player.getHp() > 0 && ai.getHp() > 0) {
            showHealth(player, ai);

            Attack pAtk = choosePlayerAttack(player);
            Attack aAtk = aiController.choose(ai, player, lastPlayerAttack);

            executeTurn(player, ai, pAtk, aAtk);
            lastPlayerAttack = pAtk;
        }

        if (player.getHp() <= 0 && ai.getHp() <= 0) {
            System.out.println("Draw!");
            Stats.draws++;
        } else if (player.getHp() <= 0) {
            System.out.println("You lose!");
            Stats.losses++;
        } else {
            System.out.println("You win!");
            Stats.wins++;
        }
        Stats.battles++;
    }

    private void executeTurn(Player player, Player ai, Attack pAtk, Attack aAtk) {
        animate("You", pAtk);
        resolveAttack(player, ai, pAtk);

        animate("AI", aAtk);
        resolveAttack(ai, player, aAtk);

        player.applyPoison();
        ai.applyPoison();
    }

    private void resolveAttack(Player attacker, Player defender, Attack atk) {
        attacker.recordUse(atk);
        int dmg = atk.getDamage();

        if (dmg < 0) {
            attacker.heal(-dmg);
            System.out.println("Healed!");
            return;
        }

        if (dmg == 0) {
            attacker.setBlocked(true);
            System.out.println("Blocking next attack!");
            return;
        }

        if (defender.isBlocked()) {
            dmg /= 2;
            defender.setBlocked(false);
            System.out.println("Blocked! Damage reduced.");
        }

        defender.takeDamage(dmg);
        System.out.println("Hit for " + dmg + " damage!");

        if (atk.getName().contains("Poison") && random.nextDouble() < 0.4) {
            defender.setPoisoned(true);
            System.out.println("Poisoned!");
        }
    }

    private Attack choosePlayerAttack(Player player) {
        while (true) {
            System.out.println("\nChoose your attack:");
            int i = 1;
            for (String name : GameConstants.ATTACK_ORDER) {
                Attack a = new Attack(name);
                String status = player.canUse(a) ? "OK" : "MAXED";
                System.out.println(i + ". " + name + " [" + status + "]");
                i++;
            }

            System.out.print("Enter number: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= GameConstants.ATTACK_ORDER.size()) {
                    Attack a = new Attack(GameConstants.ATTACK_ORDER.get(choice - 1));
                    if (player.canUse(a)) {
                    	return a;
                    }
                }
            } catch (Exception ignored) {}
            System.out.println("Invalid choice. Try again.");
        }
    }

    private void animate(String actor, Attack atk) {
        System.out.println(actor + " uses " + atk.getName());
        List<String> frames = atk.getAnimation();
        if (frames != null) {
            for (String frame : frames) {
                System.out.println(frame);
                try { 
                	Thread.sleep(80); 
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void showHealth(Player p, Player a) {
        System.out.println("Player HP: " + p.getHp());
        System.out.println("AI HP: " + a.getHp());
    }

    private boolean checkEnd(Stats stats, Player p, Player a) {
        if (p.getHp() <= 0 && a.getHp() <= 0) {
            System.out.println("Draw!");
            Stats.draws++;
            Stats.battles++;
            return true;
        }

        if (p.getHp() <= 0) {
            System.out.println("You lose!");
            Stats.losses++;
            Stats.battles++;
            return true;
        }

        if (a.getHp() <= 0) {
            System.out.println("You win!");
            Stats.wins++;
            Stats.battles++;
            return true;
        }

        return false;
    }
}
