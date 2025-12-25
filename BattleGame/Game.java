package battlegame;

/**
 * @author - Saahib M. Singh
 * <p>This is the class for running the game. </p>
 */
import java.util.Scanner;

public class Game {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
			Stats stats = new Stats();
			BattleEngine engine = new BattleEngine();

			while (true) {
			    System.out.println("\n=== EMOJI BATTLE ===");
			    System.out.println("1. Start Game");
			    System.out.println("2. How to Play");
			    System.out.println("3. Stats");
			    System.out.println("4. Exit");
			    System.out.print("Choice: ");

			    String c = scanner.nextLine();
				System.out.println("Enter your name:");
				String name = scanner.nextLine();
				System.out.println("Welcome, " + name + "!");
			    switch (c) {
			        case "1":
			            System.out.println("Choose mode:");
			            System.out.println("1. 5 Turns");
			            System.out.println("2. Fight to 0 HP");
			            String mode = scanner.nextLine();
			            if (mode.equals("1")) {
			            	engine.runTurnsMode(stats);
			            } else {
			            	engine.runHealthMode(stats);
			            }
			            
			            break;

			        case "2":
			            System.out.println("\nHow to Play:");
			            System.out.println("- Choose attacks with limited uses");
			            System.out.println("- Shield reduces damage");
			            System.out.println("- Poison deals damage over time");
			            System.out.println("- Ultimate unlocks below 30 HP");
			            break;

			        case "3":
			            System.out.println("\nStats:");
			            System.out.println("Wins: " + Stats.wins);
			            System.out.println("Losses: " + Stats.losses);
			            System.out.println("Draws: " + Stats.draws);
			            System.out.println("Battles: " + Stats.battles);
			            break;

			        case "4":
			            System.out.println("Goodbye!");
			            return;

			        default:
			            System.out.println("Invalid choice.");
			    }
			}
		}
    }
}
