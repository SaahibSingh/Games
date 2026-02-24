/**
 * Factory class to create Piece instances from board character representation.
 * Maps 'P'→Pawn, 'R'→Rook, etc. Returns null for empty squares ('-').
 */
public class PieceFactory {

    /**
     * Creates a Piece instance from its character representation on the board.
     * @param p character from board ('P','p','R','r', etc. or '-')
     * @return corresponding Piece instance, or null for empty square
     */
    public static Piece createFromChar(char p) {
        if (p == '-') return null;
        boolean isWhite = Character.isUpperCase(p);
        
        switch (Character.toLowerCase(p)) {
            case 'p': return new Pawn(isWhite);
            case 'r': return new Rook(isWhite);
            case 'n': return new Knight(isWhite);
            case 'b': return new Bishop(isWhite);
            case 'q': return new Queen(isWhite);
            case 'k': return new King(isWhite);
            default: return null;  // Invalid piece character
        }
    }
}
