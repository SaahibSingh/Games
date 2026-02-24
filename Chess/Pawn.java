/**
 * Pawn piece implementation. Supports:
 * - Forward 1 square (non-capture)
 * - Forward 2 squares from starting position
 * - Diagonal 1 square capture
 * Does NOT implement en passant or promotion (advanced features).
 */
public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
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

        int direction = isWhite ? -1 : 1;  // White moves up (row--), Black moves down (row++)
        int startRow = isWhite ? 6 : 1;    // White pawns start row 6, Black row 1

        int dr = toR - fromR;
        int dc = Math.abs(toC - fromC);

        // Forward movement (no capture)
        if (dc == 0) {
            if (toPiece != '-') {
              return false;  // Must be empty
            }
            
            // Single step forward
            if (dr == direction) {
              return true;
            }
            
            // Double step from starting row only
            if (fromR == startRow && dr == 2 * direction) {
                int midRow = fromR + direction;
                return board.getPiece(midRow, fromC) == '-';
            }
          
            return false;
        }

        // Diagonal capture (1 row forward, 1 column sideways)
        if (dc == 1 && dr == direction) {
            return toPiece != '-';  // Must capture enemy piece
        }

        return false;
    }
}
