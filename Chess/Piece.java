/**
 * Abstract base class for all chess pieces. Defines the interface for movement validation.
 * White pieces use uppercase letters (P,N,B,R,Q,K), black uses lowercase (p,n,b,r,q,k).
 */
public abstract class Piece {
    protected final boolean isWhite; // true = white (uppercase), false = black (lowercase)

    /**
     * Constructs a chess piece with specified color.
     * @param isWhite true for white piece, false for black piece
     */
    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    /**
     * Returns true if this is a white piece.
     * @return true for white, false for black
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * Validates if a move is legal for this piece type.
     * Must check path clearance, movement pattern, and capture rules.
     * @param fromR starting row (0-7)
     * @param fromC starting column (0-7)  
     * @param toR destination row (0-7)
     * @param toC destination column (0-7)
     * @param board the game board state
     * @return true if move is legal, false otherwise
     */
    public abstract boolean isValidMove(int fromR, int fromC, int toR, int toC, GameBoard board);
}
