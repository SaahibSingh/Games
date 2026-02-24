public class Knight extends Piece {
    public Knight(boolean isPlayerOnePiece) {
        super(isPlayerOnePiece);
    }

    @Override
    public boolean isValidMove(int fromX, int fromY, int toX, int toY, GameBoard board) {
        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }
}
