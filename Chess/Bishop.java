/**
 * Bishop piece implementation. Moves any number of squares diagonally.
 * Cannot jump over other pieces. Can capture enemy pieces.
 */
public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int fromR, int fromC, int toR, int toC, GameBoard board) {
        if (!board.inBounds(toR, toC)) {
          return false;
        }
      
        if (fromR == toR && fromC == toC) {
          return false;
        }

        char fromPiece = board.getPiece(fromR, fromC);
        char toPiece = board.getPiece(toR, toC);

        // Cannot capture own pieces
        if (PieceColorUtil.sameColor(fromPiece, toPiece)) {
          return false;
        }

        // Must move diagonally (equal row and column distance)
        int dr = Math.abs(toR - fromR);
        int dc = Math.abs(toC - fromC);
        if (dr != dc) {
          return false;
        }

        // Check path is clear
        int stepR = (toR > fromR) ? 1 : -1;
        int stepC = (toC > fromC) ? 1 : -1;
        
        int r = fromR + stepR;
        int c = fromC + stepC;
        
        while (r != toR || c != toC) {
            if (board.getPiece(r, c) != '-') {
              return false;  // Path blocked
            }
            r += stepR;
            c += stepC;
        }
        
        return true;
    }
}
