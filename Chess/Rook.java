/**
 * Rook piece implementation. Moves any number of squares horizontally or vertically.
 * Cannot jump over other pieces. Can capture enemy pieces.
 */
public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int fromR, int fromC, int toR, int toC, GameBoard board) {
        if (!board.inBounds(toR, toC)) return false;
        if (fromR == toR && fromC == toC) return false;

        char fromPiece = board.getPiece(fromR, fromC);
        char toPiece = board.getPiece(toR, toC);

        // Cannot capture own pieces
        if (PieceColorUtil.sameColor(fromPiece, toPiece)) return false;

        // Must move horizontally or vertically
        if (fromR != toR && fromC != toC) return false;

        // Check path is clear
        int stepR = Integer.compare(toR, fromR);  // -1, 0, or 1
        int stepC = Integer.compare(toC, fromC);
        
        int r = fromR + stepR;
        int c = fromC + stepC;
        
        while (r != toR || c != toC) {
            if (board.getPiece(r, c) != '-') return false;  // Path blocked
            r += stepR;
            c += stepC;
        }
        
        return true;
    }
}
