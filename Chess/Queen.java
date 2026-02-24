/**
 * Queen piece implementation. Combines rook + bishop movement.
 * Moves any number of squares horizontally, vertically, or diagonally.
 * Cannot jump over other pieces. Can capture enemy pieces.
 */
public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int fromR, int fromC, int toR, int toC, GameBoard board) {
        // Delegate to Rook or Bishop logic
        return new Rook(isWhite).isValidMove(fromR, fromC, toR, toC, board) ||
               new Bishop(isWhite).isValidMove(fromR, fromC, toR, toC, board);
    }
}
