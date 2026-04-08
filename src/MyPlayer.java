import java.awt.*;

public class MyPlayer {

    public Chip[][] gameBoard;

    private final int ROWS = 3;
    private final int COLS = 3;

    // boardResults[c1][c2][c3]
    private static boolean[][][] boardResults = new boolean[4][4][4];

    public MyPlayer() {
        precomputeWinningBoards();
    }

    // ---------------- PRECOMPUTE ----------------
    private void precomputeWinningBoards() {

        boardResults[1][0][0] = false; // poison cookie

        for (int sum = 1; sum <= 9; sum++) {
            for (int c1 = 0; c1 <= 3; c1++) {
                for (int c2 = 0; c2 <= c1; c2++) {
                    for (int c3 = 0; c3 <= c2; c3++) {

                        if (c1 + c2 + c3 != sum) continue;
                        if (c1 == 1 && c2 == 0 && c3 == 0) continue;

                        boardResults[c1][c2][c3] =
                                computeIsWinning(c1, c2, c3);
                    }
                }
            }
        }
    }

    // ---------------- WINNING CHECK ----------------
    private boolean computeIsWinning(int c1, int c2, int c3) {

        int[] board = {c1, c2, c3};

        for (int col = 0; col < COLS; col++) {
            for (int r = 0; r < board[col]; r++) {

                int[] newBoard = board.clone();

                for (int i = col; i < COLS; i++) {
                    newBoard[i] = Math.min(newBoard[i], r);
                }

                int[] norm = normalizeBoard(newBoard);

                if (!boardResults[norm[0]][norm[1]][norm[2]])
                    return true;
            }
        }
        return false;
    }

    // ---------------- NORMALIZE ----------------
    private int[] normalizeBoard(int[] heights) {

        int[] sorted = heights.clone();

        for (int i = 0; i < 2; i++) {
            for (int j = i + 1; j < 3; j++) {
                if (sorted[i] < sorted[j]) {
                    int t = sorted[i];
                    sorted[i] = sorted[j];
                    sorted[j] = t;
                }
            }
        }
        return sorted;
    }

    // ---------------- GET HEIGHTS ----------------
    private int[] getColumnHeights() {

        int[] heights = new int[COLS];

        for (int c = 0; c < COLS; c++) {
            int count = 0;

            for (int r = 0; r < ROWS; r++) {
                if (gameBoard[r][c] != null)
                    count++;
            }

            heights[c] = count;
        }

        return heights;
    }

    // ---------------- CHOOSE MOVE ----------------
    private Point chooseMove(int[] heights) {

        int[] norm = normalizeBoard(heights);
        boolean winning =
                boardResults[norm[0]][norm[1]][norm[2]];

        Point bestMove = null;
        int maxTiles = -1;

        // Try every legal bite
        for (int c = 0; c < COLS; c++) {
            for (int r = 0; r < heights[c]; r++) {

                // skip poison cookie
                if (r == 0 && c == 0) continue;

                int[] newHeights = heights.clone();

                // simulate chomp
                for (int i = c; i < COLS; i++)
                    newHeights[i] = Math.min(newHeights[i], r);

                int[] newNorm = normalizeBoard(newHeights);

                boolean nextWinning =
                        boardResults[newNorm[0]]
                                [newNorm[1]]
                                [newNorm[2]];

                // PERFECT PLAY
                if (winning && !nextWinning) {
                    return new Point(r, c);
                }

                // STALL IF LOSING
                if (!winning) {
                    int tiles =
                            newNorm[0]
                                    + newNorm[1]
                                    + newNorm[2];

                    if (tiles > maxTiles) {
                        maxTiles = tiles;
                        bestMove = new Point(r, c);
                    }
                }
            }
        }

        if (bestMove != null)
            return bestMove;

        // fallback safety move
        for (int c = 0; c < COLS; c++)
            for (int r = 0; r < heights[c]; r++)
                if (!(r == 0 && c == 0))
                    return new Point(r, c);

        return new Point(0,1);
    }

    // ---------------- PUBLIC MOVE ----------------
    public Point move(Chip[][] pBoard) {

        gameBoard = pBoard;

        int[] heights = getColumnHeights();

        return chooseMove(heights);
    }
}