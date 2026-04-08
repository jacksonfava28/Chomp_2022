import java.util.HashMap;

public class MyChomp {

    // stores whether a board is winning or losing
    static HashMap<String, Boolean> boardResults = new HashMap<>();

    // ---------------- MOVE CLASS ----------------
    static class Move {
        int col;
        int height;
        String resultBoard;

        Move(int c, int h, String r) {
            col = c;
            height = h;
            resultBoard = r;
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        // base losing position
        boardResults.put("1.0.0", false);

        for (int c1 = 0; c1 <= 3; c1++) {
            for (int c2 = 0; c2 <= c1; c2++) {
                for (int c3 = 0; c3 <= c2; c3++) {

                    if (c1 == 0 && c2 == 0 && c3 == 0)
                        continue;

                    String key = c1 + "." + c2 + "." + c3;

                    if (key.equals("1.0.0")) {
                        System.out.println(key + " : Losing (base case)");
                        continue;
                    }

                    boolean winning = isWinning(c1, c2, c3);
                    boardResults.put(key, winning);

                    if (winning)
                        System.out.println(key + " : Winning");
                    else
                        System.out.println(key + " : Losing");

                    System.out.println("Moves to:");
                    findNextBoards(c1, c2, c3);
                    System.out.println();
                }
            }
        }

        // ---------- TEST SOLVER ----------
        Move best = chooseMove(3,3,3);

        System.out.println("AI chooses move:");
        System.out.println(
                "(" + best.col + "," + best.height + ") -> "
                        + best.resultBoard
        );
    }

    // ---------------- WINNING CHECK ----------------
    public static boolean isWinning(int c1, int c2, int c3) {

        int[] board = {c1, c2, c3};

        for (int col = 0; col < 3; col++) {
            for (int height = 0; height < board[col]; height++) {

                int[] newBoard = board.clone();

                for (int i = col; i < 3; i++) {
                    newBoard[i] = Math.min(newBoard[i], height);
                }

                int n1 = newBoard[0];
                int n2 = newBoard[1];
                int n3 = newBoard[2];

                if (n1 >= n2 && n2 >= n3) {

                    String nextKey = n1 + "." + n2 + "." + n3;

                    Boolean result = boardResults.get(nextKey);

                    // winning if we can move to losing
                    if (result != null && result == false) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // ---------------- PRINT MOVES ----------------
    public static void findNextBoards(int c1, int c2, int c3) {

        int[] board = {c1, c2, c3};

        for (int col = 0; col < 3; col++) {
            for (int height = 0; height < board[col]; height++) {

                int[] newBoard = board.clone();

                for (int i = col; i < 3; i++) {
                    newBoard[i] = Math.min(newBoard[i], height);
                }

                int n1 = newBoard[0];
                int n2 = newBoard[1];
                int n3 = newBoard[2];

                if (n1 >= n2 && n2 >= n3) {

                    String nextKey = n1 + "." + n2 + "." + n3;

                    Boolean result = boardResults.get(nextKey);

                    String type =
                            (result != null && result == false)
                                    ? "LOSING move (good)"
                                    : "winning move";

                    System.out.println(
                            "  Move (" + col + "," + height + ") -> "
                                    + nextKey + " : " + type
                    );
                }
            }
        }
    }

    // ---------------- SOLVER MOVE CHOICE ----------------
    public static Move chooseMove(int c1, int c2, int c3) {

        int[] board = {c1, c2, c3};

        Move best = null;
        int maxCookies = -1;

        for (int col = 0; col < 3; col++) {
            for (int height = 0; height < board[col]; height++) {

                int[] newBoard = board.clone();

                for (int i = col; i < 3; i++) {
                    newBoard[i] = Math.min(newBoard[i], height);
                }

                int n1 = newBoard[0];
                int n2 = newBoard[1];
                int n3 = newBoard[2];

                if (!(n1 >= n2 && n2 >= n3))
                    continue;

                String nextKey = n1 + "." + n2 + "." + n3;

                Boolean result = boardResults.get(nextKey);

                int cookies = n1 + n2 + n3;

                // PERFECT STRATEGY:
                // move opponent to losing board
                if (result != null && result == false) {
                    return new Move(col, height, nextKey);
                }

                // fallback stall move
                if (cookies > maxCookies) {
                    maxCookies = cookies;
                    best = new Move(col, height, nextKey);
                }
            }
        }

        return best;
    }
}