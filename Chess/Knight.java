/**
 * Knight piece implementation. Moves in L-shape: 2 squares one direction, 
 * then 1 square perpendicular. Can jump over other pieces. Can capture enemy pieces.
 */
public class Knight extends Piece {

    public Knight(boolean isWhite) {
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

        int dr = Math.abs(toR - fromR);
        int dc = Math.abs(toC - fromC);

        // L-shaped move: (2,1) or (1,2)
        return (dr == 2 && dc == 1) || (dr == 1 && dc == 2);
    }
}
