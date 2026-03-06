import java.util.HashMap;

public class MyChomp {

    static HashMap<String, Boolean> memo = new HashMap<>();

    public static void main(String[] args) {

        for (int c1 = 3; c1 >= 0; c1--) {
            for (int c2 = c1; c2 >= 0; c2--) {
                for (int c3 = c2; c3 >= 0; c3--) {

                    if (c1 == 0 && c2 == 0 && c3 == 0) continue;

                    boolean win = isWinning(c1, c2, c3);

                    if (win) {
                        System.out.println(c1 + "." + c2 + "." + c3 + " : Winning");
                    } else {
                        System.out.println(c1 + "." + c2 + "." + c3 + " : Losing");
                    }
                }
            }
        }
    }

    public static boolean isWinning(int c1, int c2, int c3) {

        // base case
        if (c1 == 1 && c2 == 0 && c3 == 0) {
            return false; // losing
        }

        String key = c1 + "." + c2 + "." + c3;

        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int[] board = {c1, c2, c3};

        // try every possible move
        for (int col = 0; col < 3; col++) {
            for (int height = 0; height < board[col]; height++) {

                int[] newBoard = board.clone();

                for (int i = col; i < 3; i++) {
                    newBoard[i] = Math.min(newBoard[i], height);
                }

                int n1 = newBoard[0];
                int n2 = newBoard[1];
                int n3 = newBoard[2];

                // check valid board ordering
                if (n1 >= n2 && n2 >= n3) {

                    if (!isWinning(n1, n2, n3)) {
                        memo.put(key, true);
                        return true;
                    }
                }
            }
        }

        memo.put(key, false);
        return false;
    }
}