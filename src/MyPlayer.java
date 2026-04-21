import java.awt.*;
import java.util.Arrays;

public class MyPlayer {

    // reference to GUI board
    public Chip[][] gameBoard;

    private final int ROWS = 3;
    private final int COLS = 3;

    // boardResults[c1][c2][c3]
    // true  = winning position
    // false = losing position
    private boolean[][][] boardResults =
            new boolean[4][4][4];

    public MyPlayer() {
        // precompute all positions once
        precomputeWinningBoards();
    }

    // ---------------- PRECOMPUTE WINNING BOARDS ----------------
    private void precomputeWinningBoards() {

        // poison cookie alone → losing
        boardResults[1][0][0] = false;

        // compute positions by increasing size
        // (dynamic programming)
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

    // ---------------- WINNING TEST ----------------
    // same logic as MyChomp solver
    private boolean computeIsWinning(int c1, int c2, int c3) {

        int[] board = {c1, c2, c3};

        for (int col = 0; col < COLS; col++) {
            for (int newHeight = 0;
                 newHeight < board[col];
                 newHeight++) {

                int[] newBoard = board.clone();

                // simulate bite
                for (int i = col; i < COLS; i++)
                    newBoard[i] =
                            Math.min(newBoard[i], newHeight);

                int[] norm = normalizeBoard(newBoard);

                // winning if opponent gets losing board
                if (!boardResults[norm[0]]
                        [norm[1]]
                        [norm[2]])
                    return true;
            }
        }
        return false;
    }

    // ---------------- NORMALIZE BOARD ----------------
    // sorts columns descending so states match solver
    private int[] normalizeBoard(int[] heights) {

        int[] sorted = heights.clone();

        for (int i = 0; i < 2; i++)
            for (int j = i + 1; j < 3; j++)
                if (sorted[i] < sorted[j]) {
                    int t = sorted[i];
                    sorted[i] = sorted[j];
                    sorted[j] = t;
                }

        return sorted;
    }

    // ---------------- READ CURRENT BOARD ----------------
    // counts how many cookies remain in each column
    private int[] getColumnHeights() {

        int[] heights = new int[COLS];

        for (int c = 0; c < COLS; c++) {

            int count = 0;

            for (int r = 0; r < ROWS; r++) {
                if (gameBoard[r][c].isAlive)
                    count++;
            }

            heights[c] = count;
        }

        System.out.println(Arrays.toString(heights));
        return heights;
    }

    // ---------------- CHOOSE MOVE ----------------
    private Point chooseMove(int[] heights) {

        int[] norm = normalizeBoard(heights);

        boolean winning =
                boardResults[norm[0]][norm[1]][norm[2]];

        Point bestMove = null;
        int maxTiles = -1;

        for (int c = 0; c < COLS; c++) {

            for (int newHeight = 0;
                 newHeight < heights[c];
                 newHeight++) {

                // never intentionally eat poison
                if (c == 0 && newHeight == 0) continue;

                int[] newHeights = heights.clone();

                // simulate move
                for (int i = c; i < COLS; i++)
                    newHeights[i] =
                            Math.min(newHeights[i], newHeight);

                int[] newNorm =
                        normalizeBoard(newHeights);

                boolean nextWinning =
                        boardResults[newNorm[0]]
                                [newNorm[1]]
                                [newNorm[2]];

                // convert solver move → GUI click position
                int oldHeight = heights[c];

                int rowClicked =
                        ROWS - oldHeight + newHeight;

                // ignore invalid clicks
                if (rowClicked < 0 ||
                        rowClicked >= ROWS)
                    continue;

                if (!gameBoard[rowClicked][c].isAlive)
                    continue;

                // PERFECT PLAY:
                // move opponent into losing position
                if (winning && !nextWinning) {
                    return new Point(rowClicked, c);
                }

                // LOSING POSITION:
                // delay loss as long as possible
                if (!winning) {

                    int tiles =
                            newNorm[0]
                                    + newNorm[1]
                                    + newNorm[2];

                    if (tiles > maxTiles) {
                        maxTiles = tiles;
                        bestMove =
                                new Point(rowClicked, c);
                    }
                }
            }
        }

        if (bestMove != null)
            return bestMove;

        // emergency fallback move
        for (int r = ROWS - 1; r >= 0; r--)
            for (int c = 0; c < COLS; c++)
                if (gameBoard[r][c] != null &&
                        !(r == ROWS - 1 && c == 0))
                    return new Point(r, c);

        // final safety return
        return new Point(ROWS - 1, 1);
    }

    // ---------------- PUBLIC MOVE ----------------
    // called by game engine
    public Point move(Chip[][] pBoard) {

        gameBoard = pBoard;

        int[] heights = getColumnHeights();

        // debug print
        System.out.println(
                heights[0] + "." +
                        heights[1] + "." +
                        heights[2]
        );

        return chooseMove(heights);
    }
}