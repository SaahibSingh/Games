import java.util.Scanner; //Import

/*
@author - Saahib M. Singh
*/

public class TicTacToeRunner {
    public static void main(String[] args) {
        System.out.println("Welcome to Tic-Tac-Toe!");
        TicTacToe game = new TicTacToe();
        game.printBoard();
        
        Scanner scanner = new Scanner(System.in);
        
        while (!game.checkWin() && game.checkTurn()) {
            System.out.println("Enter Your Row Pick: ");
            int row = scanner.nextInt();
            System.out.println("Enter Your Col Pick: ");
            int col = scanner.nextInt();
            
            if (game.pickLocation(row, col)) {
                game.takeTurn(row, col);
                game.printBoard(); 
            } else {
                System.out.println("That space is taken, or you entered an invalid row/col");
            }
        }
        
        System.out.println(game.checkWin() ? (game.getTurn() % 2 == 0 ? "X" : "O") + "'s won!" : "Tie!");
    }
}
