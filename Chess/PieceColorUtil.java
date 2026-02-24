/**
 * Utility class for determining piece colors and ownership in a chess game.
 * Provides static methods to check if pieces belong to white/black players.
 */
public class PieceColorUtil {
    
    /**
     * Checks if a piece character represents a white piece (uppercase letters).
     * @param p the piece character to check
     * @return true if white piece, false otherwise
     */
    public static boolean isWhitePiece(char p) {
        return Character.isUpperCase(p) && p != '-';
    }

    /**
     * Checks if a piece character represents a black piece (lowercase letters).
     * @param p the piece character to check
     * @return true if black piece, false otherwise
     */
    public static boolean isBlackPiece(char p) {
        return Character.isLowerCase(p) && p != '-';
    }

    /**
     * Determines if two pieces belong to the same player.
     * @param a first piece character
     * @param b second piece character
     * @return true if same color, false otherwise
     */
    public static boolean sameColor(char a, char b) {
        if (a == '-' || b == '-') return false;
        return (isWhitePiece(a) && isWhitePiece(b)) || (isBlackPiece(a) && isBlackPiece(b));
    }
}
