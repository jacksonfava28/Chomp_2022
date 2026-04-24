import java.awt.Point;
import java.util.Arrays;

public class MyPlayer {

    public MyChomp solver = new MyChomp();


    // main AI move
    public Point move(Chip[][] board) {

        // convert board into height array
        int[] heights = computeHeights(board);

        System.out.println("Heights: " + Arrays.toString(heights));

        // try to find a winning move using solver
        int[] move = solver.findWinningMove(heights);

        if (move != null) {

            System.out.println("Winning move -> "
                    + move[0] + "," + move[1]);

            return new Point(move[0], move[1]);
        }

        // otherwise use fallback strategy
        Point fallback = fallbackMove(heights);

        System.out.println("Fallback move -> "
                + fallback.x + "," + fallback.y);

        return fallback;
    }


    // convert board into column heights
    private int[] computeHeights(Chip[][] board) {

        int rows = board.length;
        int cols = board[0].length;

        int[] heights = new int[cols];

        for (int c = 0; c < cols; c++) {

            int count = 0;

            for (int r = 0; r < rows; r++) {

                // count only live chips in this column
                if (board[r][c] != null &&
                        board[r][c].isAlive)
                    count++;
            }

            heights[c] = count;
        }

        return heights;
    }


    // fallback move if no winning move found
    private Point fallbackMove(int[] heights) {

        // pick rightmost non-empty column
        for (int c = heights.length - 1; c >= 0; c--) {

            if (heights[c] > 0)
                return new Point(heights[c] - 1, c);
        }

        // default move (should rarely happen)
        return new Point(1,1);
    }
}