import java.util.HashMap;

public class MyChomp {

    static HashMap<String, Boolean> boardResults = new HashMap<>();

    public static void main(String[] args) {

        // base losing position
        boardResults.put("1.0.0", false);

        for (int c1 = 0; c1 <= 3; c1++) {
            for (int c2 = 0; c2 <= c1; c2++) {
                for (int c3 = 0; c3 <= c2; c3++) {

                    if (c1 == 0 && c2 == 0 && c3 == 0) continue;

                    String key = c1 + "." + c2 + "." + c3;

                    if (key.equals("1.0.0")) {
                        System.out.println(key + " : Losing (base case)");
                        continue;
                    }

                    boolean winning = isWinning(c1, c2, c3);

                    boardResults.put(key, winning);

                    if (winning) {
                        System.out.println(key + " : Winning");
                    } else {
                        System.out.println(key + " : Losing");
                    }

                    System.out.println("Moves to:");
                    findNextBoards(c1, c2, c3);
                    System.out.println();
                }
            }
        }
    }

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

                    if (result != null && result == false) {
                        return true; // move to losing board
                    }
                }
            }
        }

        return false; // all moves go to winning boards
    }

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
                    System.out.println("  " + n1 + "." + n2 + "." + n3);
                }
            }
        }
    }
}
//have it deliniate what makes it winning or losing and atatch a move to it so it can play it

// idea: for the button if in a winning board have it pick any boards in possible moves that are lose boards
// idea: if the board is a losing board have it pick the board with the most tiles left as to hold onto the chance as long as possible
//