import java.util.HashMap;

public class MyChomp {

    // Stores whether a board position is winning (true)
    // or losing (false)
    // Key format: "c1.c2.c3"
    static HashMap<String, Boolean> boardResults = new HashMap<>();

    // ---------------- MOVE CLASS ----------------
    // Represents one possible move choice
    static class Move {
        int col;          // column clicked
        int height;       // new height after chomp
        String resultBoard; // resulting board string

        Move(int c, int h, String r) {
            col = c;
            height = h;
            resultBoard = r;
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        // Base losing position:
        // only poison cookie remains
        boardResults.put("1.0.0", false);

        // Generate ALL valid board shapes
        // (columns must be non-increasing)
        for (int c1 = 0; c1 <= 3; c1++) {
            for (int c2 = 0; c2 <= c1; c2++) {
                for (int c3 = 0; c3 <= c2; c3++) {

                    // ignore empty board
                    if (c1 == 0 && c2 == 0 && c3 == 0)
                        continue;

                    String key = c1 + "." + c2 + "." + c3;

                    // already known base case
                    if (key.equals("1.0.0")) {
                        System.out.println(key + " : Losing (base case)");
                        continue;
                    }

                    // determine if position is winning
                    boolean winning = isWinning(c1, c2, c3);
                    boardResults.put(key, winning);

                    // print result
                    if (winning)
                        System.out.println(key + " : Winning");
                    else
                        System.out.println(key + " : Losing");

                    // show all reachable boards
                    System.out.println("Moves to:");
                    findNextBoards(c1, c2, c3);
                    System.out.println();
                }
            }
        }

        // ---------- TEST SOLVER ----------
        // Ask AI for best move from starting board
        Move best = chooseMove(3,3,3);

        System.out.println("AI chooses move:");
        System.out.println(
                "(" + best.col + "," + best.height + ") -> "
                        + best.resultBoard
        );
    }

    // ---------------- WINNING CHECK ----------------
    // A board is winning if ANY legal move
    // leads to a losing board.
    public static boolean isWinning(int c1, int c2, int c3) {

        int[] board = {c1, c2, c3};

        // try every column
        for (int col = 0; col < 3; col++) {

            // try every smaller height
            for (int height = 0; height < board[col]; height++) {

                // simulate chomp
                int[] newBoard = board.clone();

                // everything to the right is reduced
                for (int i = col; i < 3; i++) {
                    newBoard[i] = Math.min(newBoard[i], height);
                }

                int n1 = newBoard[0];
                int n2 = newBoard[1];
                int n3 = newBoard[2];

                // ensure valid board ordering
                if (n1 >= n2 && n2 >= n3) {

                    String nextKey = n1 + "." + n2 + "." + n3;

                    Boolean result = boardResults.get(nextKey);

                    // WINNING RULE:
                    // if we can move opponent to losing position
                    if (result != null && result == false) {
                        return true;
                    }
                }
            }
        }

        // no winning move found
        return false;
    }

    // ---------------- PRINT MOVES ----------------
    // Displays every possible move from a board
    public static void findNextBoards(int c1, int c2, int c3) {

        int[] board = {c1, c2, c3};

        for (int col = 0; col < 3; col++) {
            for (int height = 0; height < board[col]; height++) {

                int[] newBoard = board.clone();

                // simulate chomp
                for (int i = col; i < 3; i++) {
                    newBoard[i] = Math.min(newBoard[i], height);
                }

                int n1 = newBoard[0];
                int n2 = newBoard[1];
                int n3 = newBoard[2];

                if (n1 >= n2 && n2 >= n3) {

                    String nextKey = n1 + "." + n2 + "." + n3;

                    Boolean result = boardResults.get(nextKey);

                    // classify move quality
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
    // Chooses best move using perfect play
    public static Move chooseMove(int c1, int c2, int c3) {

        int[] board = {c1, c2, c3};

        Move best = null;
        int maxCookies = -1;

        for (int col = 0; col < 3; col++) {
            for (int height = 0; height < board[col]; height++) {

                int[] newBoard = board.clone();

                // simulate chomp
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
                // immediately choose move that forces loss
                if (result != null && result == false) {
                    return new Move(col, height, nextKey);
                }

                // fallback strategy:
                // delay loss as long as possible
                if (cookies > maxCookies) {
                    maxCookies = cookies;
                    best = new Move(col, height, nextKey);
                }
            }
        }

        return best;
    }
}