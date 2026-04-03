import java.awt.*;

public class MyPlayer {

    public Chip[][] gameBoard;
    private final int ROWS = 3;
    private final int COLS = 3;
    private static boolean[][][] boardResults = new boolean[4][4][4];

    public MyPlayer() {
        precomputeWinningBoards();
    }

    // Precompute which normalized boards are winning or losing
    private void precomputeWinningBoards() {
        boardResults[1][0][0] = false; // poison cookie losing board
        for (int sum = 1; sum <= 9; sum++) {
            for (int c1 = 0; c1 <= 3; c1++) {
                for (int c2 = 0; c2 <= c1; c2++) {
                    for (int c3 = 0; c3 <= c2; c3++) {
                        if (c1 + c2 + c3 != sum) continue;
                        if (c1 == 1 && c2 == 0 && c3 == 0) continue;
                        boardResults[c1][c2][c3] = computeIsWinning(c1, c2, c3);
                    }
                }
            }
        }
    }

    // Determine if a board is winning
    private boolean computeIsWinning(int c1, int c2, int c3) {
        int[] board = {c1, c2, c3};
        for (int col = 0; col < COLS; col++) {
            for (int h = 0; h < board[col]; h++) {
                int[] newBoard = board.clone();
                for (int i = col; i < COLS; i++) newBoard[i] = Math.min(newBoard[i], h);
                int[] norm = normalizeBoard(newBoard);
                if (!boardResults[norm[0]][norm[1]][norm[2]]) return true;
            }
        }
        return false;
    }

    // Normalize board so c1 >= c2 >= c3
    private int[] normalizeBoard(int[] heights) {
        int[] sorted = heights.clone();
        for (int i = 0; i < 2; i++) {
            for (int j = i + 1; j < 3; j++) {
                if (sorted[i] < sorted[j]) {
                    int temp = sorted[i];
                    sorted[i] = sorted[j];
                    sorted[j] = temp;
                }
            }
        }
        return sorted;
    }

    // Get number of cookies remaining in each column (from bottom)
    private int[] getColumnHeights() {
        int[] heights = new int[COLS];
        for (int c = 0; c < COLS; c++) {
            int count = 0;
            for (int r = 0; r < ROWS; r++) {
                if (gameBoard[r][c] != null) count++;
            }
            heights[c] = ROWS - count;
        }
        return heights;
    }

    private Point chooseMove(int[] heights) {
        int[] norm = normalizeBoard(heights);
        boolean winning = boardResults[norm[0]][norm[1]][norm[2]];

        Point bestMove = null;
        int maxTiles = -1;

        // Scan all cookies from bottom-right to top-left
        for (int r = ROWS - 1; r >= 0; r--) {
            for (int c = COLS - 1; c >= 0; c--) {
                if (gameBoard[r][c] == null) continue;
                if (r == 0 && c == 0) continue; // skip poison

                int[] newHeights = heights.clone();
                for (int i = c; i < COLS; i++) newHeights[i] = Math.min(newHeights[i], r);

                int[] newNorm = normalizeBoard(newHeights);
                if (!(newNorm[0] >= newNorm[1] && newNorm[1] >= newNorm[2])) continue;

                if (winning && !boardResults[newNorm[0]][newNorm[1]][newNorm[2]]) {
                    return new Point(r, c); // force opponent to losing board
                }

                if (!winning) {
                    int tilesLeft = newNorm[0] + newNorm[1] + newNorm[2];
                    if (tilesLeft > maxTiles) {
                        maxTiles = tilesLeft;
                        bestMove = new Point(r, c);
                    }
                }
            }
        }

        if (bestMove != null) return bestMove;

        // fallback: pick first non-poison cookie
        for (int r = ROWS - 1; r >= 0; r--) {
            for (int c = COLS - 1; c >= 0; c--) {
                if (gameBoard[r][c] != null && !(r == 0 && c == 0)) return new Point(r, c);
            }
        }

        return new Point(0, 1); // last resort
    }

    public Point move(Chip[][] pBoard) {
        gameBoard = pBoard;
        int[] heights = getColumnHeights();
        return chooseMove(heights);
    }
}