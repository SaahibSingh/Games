public class GameBoard {
    private final char[][] board = new char[8][8];
    public GameBoard() { initializeBoard();  }

    private void initializeBoard() {
        // Player 1 (lowercase black) top rows 0-1
        String[] row0 = {'r','n','b','q','k','b','n','r'}; // row 0: black back rank
        String[] row1 = {'p','p','p','p','p','p','p','p'}; // pawns
        for (int j = 0; j < 8; j++) {
            board[0][j] = row0[j];
            board[1][j] = row1[j];
        }
      
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
              board[i + 2][j] = '-';
            }
        }
      
        String[] row6 = {'P','N','B','Q','K','B','N','R'};
        String[] row7 = {'p','p','p','p','p','p','p','p'};
        for (int j = 0; j < 8; j++) {
            board[6][j] = row6[j];
            board[7][j] = row7[j];
        }
    }

    public void display() {
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int i = 0; i < 8; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
          
            System.out.println();
        }
        System.out.println();
    }

    public char getPiece(int x, int y) { 
      return board[x][y]; 
    }

    public void movePiece(int fromX, int fromY, int toX, int toY) {
        board[toX][toY] = board[fromX][fromY];
        board[fromX][fromY] = '-';
    }
  
    public boolean inBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
