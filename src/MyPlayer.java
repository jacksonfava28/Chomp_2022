import java.awt.Point;
import java.util.Arrays;

public class MyPlayer {


      // MAIN MOVE FUNCTION (CALLED BY GAME)

    public Point move(Chip[][] board) {

        int[] heights = computeHeights(board);

        System.out.println("Heights: " + Arrays.toString(heights));
        printHeights(heights);

        Point winningMove = findWinningMove(heights);

        if (winningMove != null) {
            System.out.println("Winning move -> "
                    + winningMove.x + "," + winningMove.y);
            return winningMove;
        }

        Point fallback = fallbackMove(heights);

        System.out.println("Fallback move -> "
                + fallback.x + "," + fallback.y);

        return fallback;
    }


      // CONVERT GUI BOARD → HEIGHTS

    private int[] computeHeights(Chip[][] board) {

        int rows = board.length;
        int cols = board[0].length;

        int[] heights = new int[rows];

        for (int r = 0; r < rows; r++) {

            int count = 0;

            for (int c = 0; c < cols; c++) {
                if (board[r][c] != null &&
                        board[r][c].isAlive)
                    count++;
            }

            heights[r] = count;
        }

        return heights;
    }



     //  FIND WINNING MOVE

    private Point findWinningMove(int[] heights) {

        for (int r = 0; r < heights.length; r++) {

            for (int c = 0; c < heights[r]; c++) {

                // never eat poison
                if (r == 0 && c == 0)
                    continue;

                int[] next = simulateMove(heights, r, c);

                System.out.println(
                        "Try move " + r + "," + c +
                                " -> " + Arrays.toString(next));

                if (isLosingPosition(next))
                    return new Point(r, c);
            }
        }

        return null;
    }



      // SIMULATE CHOMP MOVE

    private int[] simulateMove(int[] heights, int r, int c) {

        int[] copy = heights.clone();

        for (int i = r; i < copy.length; i++) {
            copy[i] = Math.min(copy[i], c);
        }

        return copy;
    }



    //   LOSING POSITION CHECK

    private boolean isLosingPosition(int[] heights) {

        // poison only
        if (heights[0] == 1) {
            boolean empty = true;

            for (int i = 1; i < heights.length; i++)
                if (heights[i] != 0)
                    empty = false;

            if (empty)
                return true;
        }

        // opponent has winning reply?
        for (int r = 0; r < heights.length; r++) {

            for (int c = 0; c < heights[r]; c++) {

                if (r == 0 && c == 0)
                    continue;

                int[] next = simulateMove(heights, r, c);

                if (isLosingPosition(next))
                    return false;
            }
        }

        return true;
    }



      // SAFE FALLBACK MOVE

    private Point fallbackMove(int[] heights) {

        for (int r = heights.length - 1; r >= 0; r--) {
            if (heights[r] > 0)
                return new Point(r, heights[r] - 1);
        }

        return new Point(1,1);
    }



       //PRINT CHEAT SHEET FORMAT

    private void printHeights(int[] heights) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < heights.length; i++) {
            sb.append(heights[i]);

            if (i < heights.length - 1)
                sb.append(".");
        }

        System.out.println(sb.toString());
    }
}