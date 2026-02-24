/**
 * King piece implementation. Moves one square in any direction.
 * Does NOT implement castling (advanced feature requiring additional state).
 * Can capture enemy pieces.
 */
public class King extends Piece {

    public King(boolean isWhite) {
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

        // King moves exactly one square in any direction
        int dr = Math.abs(toR - fromR);
        int dc = Math.abs(toC - fromC);
        
        return dr <= 1 && dc <= 1;
    }
}
