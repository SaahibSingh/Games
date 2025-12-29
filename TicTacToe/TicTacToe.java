/*
@author - Saahib M. Singh
*/

public class TicTacToe {
    // Declare instance variables
    private String[][] board;
    private int turn;
    
    // Constructor that initializes the empty game board
    public TicTacToe() {
        turn = 0;
        board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "-";
            }
        }
    }
    
    /**
    * return the space at row and col is a valid, empty space
    * @param row - the row of the board
    * @param col - the column of the board
    * @return a boolean - true if its valid, false if its not valid and does not equal "-"
    */
    public boolean pickLocation(int row, int col) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            return board[row][col].equals("-");
        } else {
            return false;
        }
    }

    /**
    * Places an X or O at location (row, col) based on the turn value
    */
    public void takeTurn(int row, int col) {
        board[row][col] = turn % 2 == 0 ? "X" : "O";
        turn++;
    }
    
    // Returns the current turn
    public int getTurn() { return turn; }

    /**
    * @return if a single row has three X or O's
    */
    public boolean checkRow() {
        for (int i = 0; i < 3; i++) {
            int countX = 0;
            int countO = 0;
            for (int j = 0; j < 3; j++) {
                countX += board[i][j].equals("X") ? 1 : 0;
                countO += board[i][j].equals("O") ? 1 : 0;
                if (countX == 3 || countO == 3) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
    @return true if a single column has three X or O's
    */
    public boolean checkCol() {
        for (int j = 0; j < 3; j++) {
            int countX = 0;
            int countO = 0;
            for (int i = 0; i < 3; i++) {
                countX += board[i][j].equals("X") ? 1 : 0;
                countO += board[i][j].equals("O") ? 1 : 0;
                if (countX == 3 || countO == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    @returns true if a diagonal has three X or O's
    */
    public boolean checkDiag() {
        int countX1 = 0;
        int countO1 = 0;
        for (int i = 0; i < 3; i++) {
            if (board[i][i].equals("X")) {
                countX1++;
            } else if (board[i][i].equals("O")) {
                countO1++;
            }
        }
        
        if (countX1 == 3 || countO1 == 3) {
            return true;
        }
        
        int countX2 = 0;
        int countO2 = 0;
        
        for (int i = 0; i < 3; i++) {
            int col = 2 - i;
            if (board[i][col].equals("X")) {
                countX2++;
            } else if (board[i][col].equals("O")) {
                countO2++;
            }
        }
        
        return countX2 == 3 || countO2 == 3;
    }

    // Returns true if the turn count is less than the max (9)
    public boolean checkTurn() { 
        return turn < 9; 
    }
    
    // Returns true if someone has won the game
    public boolean checkWin() {
        return checkRow() || checkCol() || checkDiag();
    }

    // Prints out the board array on to the console
    public void printBoard() {
        System.out.print("  0 1 2 ");
        
        for (int row = 0; row < 3; row++) {
            System.out.print("\n" + row + " ");
            for (int col = 0; col < 3; col++) {
                System.out.print(board[row][col] + " ");
            }
        }
    }
}
