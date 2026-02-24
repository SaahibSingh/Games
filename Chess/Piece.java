public abstract class Piece {
    protected boolean isPlayerOnePiece;
    public Piece(boolean isPlayerOnePiece) { this.isPlayerOnePiece = isPlayerOnePiece; }
    public abstract boolean isValidMove(int fromX, int fromY, int toX, int toY, GameBoard board);
}
