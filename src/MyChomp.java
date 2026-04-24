import java.util.HashMap;

public class MyChomp {

    // memoization: board string -> losing or not
    HashMap<String, Boolean> memo = new HashMap<>();


    // convert height array to key
    private String key(int[] h) {

        String s = "";

        for (int i = 0; i < h.length; i++) {

            s += h[i];

            if (i < h.length - 1)
                s += ".";
        }

        return s;
    }


    // simulate move using SAME logic as your GUI updateBoard()
    private int[] simulateMove(int[] h, int row, int col) {

        int[] copy = h.clone();

        for (int c = col; c < copy.length; c++) {

            copy[c] = Math.min(copy[c], row);
        }

        return copy;
    }


    // check losing position
    public boolean isLosingPosition(int[] h) {

        String k = key(h);

        // return memoized result if already computed
        if (memo.containsKey(k))
            return memo.get(k);

        // check if this is a poison-only position
        // (only column 0 has height 1, everything else is 0)
        boolean poisonOnly = (h[0] == 1);

        for (int i = 1; i < h.length; i++) {

            if (h[i] != 0)
                poisonOnly = false;
        }

        // poison-only means losing position
        if (poisonOnly) {

            memo.put(k, true);
            return true;
        }


        // try all possible moves
        for (int col = 0; col < h.length; col++) {

            for (int row = 0; row < h[col]; row++) {

                // skip poison square (0,0)
                if (row == 0 && col == 0)
                    continue;

                int[] next = simulateMove(h, row, col);

                // if we can force opponent into losing position, this is winning
                if (isLosingPosition(next)) {

                    memo.put(k, false);
                    return false;
                }
            }
        }

        // no winning moves found
        memo.put(k, true);
        return true;
    }


    // find a winning move (if one exists)
    public int[] findWinningMove(int[] h) {

        // test every possible move
        for (int col = 0; col < h.length; col++) {

            for (int row = 0; row < h[col]; row++) {

                // skip poison square
                if (row == 0 && col == 0)
                    continue;

                int[] next = simulateMove(h, row, col);

                // if opponent is in losing position, this move wins
                if (isLosingPosition(next))
                    return new int[]{row, col};
            }
        }

        // no winning move exists
        return null;
    }
}