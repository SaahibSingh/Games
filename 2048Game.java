//Imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Complete 2048 game implementation using Java Swing GUI.
 * Features arrow key/WASD controls, tile merging, 2048 win condition,
 * game over detection, and authentic tile colors. Perfect for CS portfolios.
 */
public class 2048Game extends JPanel implements ActionListener {
    private static final int SIZE = 4;
    private static final int TILE_SIZE = 100;
    private final int[][] grid = new int[SIZE][SIZE];
    private final Random random = new Random();
    private boolean gameWon = false;
    private boolean gameOver = false;
    private Timer timer;

    /**
     * Constructor - initializes game board, key listeners, and starting tiles.
     * Sets up Swing GUI with focusable panel and keyboard controls.
     */
    public 2048Game() {
        setPreferredSize(new Dimension(SIZE * TILE_SIZE + 20, SIZE * TILE_SIZE + 40));
        setBackground(Color.WHITE);
        setFocusable(true);
        
        // Add keyboard controls (arrows + WASD)
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver && !gameWon) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) moveUp();
                    else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) moveDown();
                    else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) moveLeft();
                    else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) moveRight();
                    repaint();
                }
            }
        });
        
        timer = new Timer(100, this);
        timer.start();
        addTile();  // Spawn first tile
        addTile();  // Spawn second tile
    }

    /**
     * Adds a new random tile (90% chance 2, 10% chance 4) to empty position.
     */
    private void addTile() {
        int[] empty = findEmpty();
        if (empty != null) {
            grid[empty[0]][empty[1]] = random.nextInt(10) < 9 ? 2 : 4;
        }
    }

    /**
     * Finds first empty position (0) on board.
     * @return int[] {row, col} or null if board full
     */
    private int[] findEmpty() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * Executes leftward move: compress + merge each row, then spawn new tile.
     */
    private void moveLeft() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            int[] newRow = compressRow(grid[i]);
            if (!arraysEqual(grid[i], newRow)) {
              moved = true;
            }
            grid[i] = newRow;
        }
      
        if (moved) {
            addTile();
            checkGameState();
        }
    }

    /**
     * Executes rightward move using reverse → left → reverse strategy.
     */
    private void moveRight() {
        reverseRows();
        moveLeft();
        reverseRows();
    }

    /**
     * Executes upward move using transpose → left → transpose strategy.
     */
    private void moveUp() {
        transpose();
        moveLeft();
        transpose();
    }

    /**
     * Executes downward move using transpose → reverse → left → reverse → transpose.
     */
    private void moveDown() {
        transpose();
        reverseRows();
        moveLeft();
        reverseRows();
        transpose();
    }

    /**
     * Compresses row left and merges adjacent equal tiles.
     * @param row Original row array
     * @return New compressed/merged row
     */
    private int[] compressRow(int[] row) {
        int[] compressed = new int[SIZE];
        int pos = 0;
        
        // Step 1: Shift non-zeros left
        for (int num : row) {
            if (num != 0) {
                compressed[pos++] = num;
            }
        }
        for (int i = pos; i < SIZE; i++) {
            compressed[i] = 0;
        }
        
        // Step 2: Merge adjacent equals
        for (int i = 0; i < SIZE - 1; i++) {
            if (compressed[i] == compressed[i + 1] && compressed[i] != 0) {
                compressed[i] *= 2;
                for (int j = i + 1; j < SIZE - 1; j++) {
                    compressed[j] = compressed[j + 1];
                }
                compressed[SIZE - 1] = 0;
                i++; // Skip merged pair
            }
        }
        return compressed;
    }

    /**
     * Reverses all rows in grid (for right/down moves).
     */
    private void reverseRows() {
        for (int i = 0; i < SIZE; i++) {
            reverseArray(grid[i]);
        }
    }

    /**
     * Transposes grid (rows ↔ columns for up/down moves).
     */
    private void transpose() {
        int[][] newGrid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                newGrid[i][j] = grid[j][i];
            }
        }
        grid = newGrid;
    }

    /**
     * Reverses single array in-place.
     * @param arr Array to reverse
     */
    private void reverseArray(int[] arr) {
        for (int i = 0; i < SIZE / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[SIZE - 1 - i];
            arr[SIZE - 1 - i] = temp;
        }
    }

    /**
     * Compares two int arrays for equality.
     * @param a First array
     * @param b Second array
     * @return true if identical
     */
    private boolean arraysEqual(int[] a, int[] b) {
        for (int i = 0; i < SIZE; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    /**
     * Checks win (2048 tile) and loss (full board, no merges possible).
     */
    private void checkGameState() {
        // Win: 2048 tile exists
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 2048) {
                    gameWon = true;
                    return;
                }
            }
        }
        
        // Loss: Board full AND no adjacent equals
        if (findEmpty() == null && !movePossible()) {
            gameOver = true;
        }
    }

    /**
     * Checks if any merge move possible by scanning adjacent tiles.
     * @param g Grid to check
     * @return true if valid move exists
     */
    private boolean movePossible(int[][] g) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (g[i][j] != 0) {
                    if (i < SIZE - 1 && g[i][j] == g[i + 1][j]) {
                      return true;
                    }
                  
                    if (j < SIZE - 1 && g[i][j] == g[i][j + 1]) {
                      return true;
                    }  
                }
            }
        }
        return false;
    }

    /**
     * Creates deep copy of current grid.
     * @param original Original grid
     * @return Independent copy
     */
    private int[][] copyGrid(int[][] original) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    /**
     * Custom paint method - renders tiles with authentic 2048 colors.
     * @param g Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Draw grid tiles
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] != 0) {
                    g.setColor(getTileColor(grid[i][j]));
                    g.fillRoundRect(j * TILE_SIZE + 10, i * TILE_SIZE + 10, 
                                  TILE_SIZE - 10, TILE_SIZE - 10, 15, 15);
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(grid[i][j]), 
                               j * TILE_SIZE + 45, i * TILE_SIZE + 65);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRoundRect(j * TILE_SIZE + 10, i * TILE_SIZE + 10, 
                                  TILE_SIZE - 10, TILE_SIZE - 10, 15, 15);
                }
            }
        }
        
        // Draw win/lose messages
        if (gameWon) {
            g.setColor(Color.GREEN);
            g.drawString("YOU WIN! 2048 reached!", 20, 450);
        } else if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("GAME OVER!", 20, 450);
        }
    }

    /**
     * Returns official 2048 tile colors by value.
     * @param value Tile number (2, 4, 8, 16...)
     * @return Corresponding Color
     */
    private Color getTileColor(int value) {
        return switch (value) {
            case 2 -> new Color(238, 228, 218);
            case 4 -> new Color(237, 224, 200);
            case 8 -> new Color(242, 177, 121);
            case 16 -> new Color(245, 149, 99);
            case 32 -> new Color(246, 124, 95);
            case 64 -> new Color(246, 94, 59);
            case 128 -> new Color(237, 207, 114);
            case 256 -> new Color(237, 204, 97);
            case 512 -> new Color(237, 200, 80);
            case 1024 -> new Color(237, 197, 63);
            case 2048 -> new Color(237, 194, 46);
            default -> Color.GRAY;
        };
    }

    /** Timer callback - triggers repaint. */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    /**
     * Main method - launches Swing application.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("2048 Game - Java Swing");
        2048Game game = new 2048Game();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
