public class MyChomp {

    public static void main(String[] args) {

        for (int c1 = 3; c1 >= 0; c1--) {
            for (int c2 = c1; c2 >= 0; c2--) {
                for (int c3 = c2; c3 >= 0; c3--) {

                    if (c1 != 0 || c2 != 0 || c3 != 0) {

                        if (oneMoveFrom100(c1, c2, c3)) {
                            System.out.println(c1 + "." + c2 + "." + c3 +
                                    " is one move from 1.0.0");
                        }

                    }
                }
            }
        }
    }

    public static boolean oneMoveFrom100(int c1, int c2, int c3) {

        int[] board = {c1, c2, c3};

        // try every possible move
        for (int col = 0; col < 3; col++) {

            for (int height = 0; height < board[col]; height++) {

                int[] newBoard = board.clone();

                // simulate the move
                for (int i = col; i < 3; i++) {
                    newBoard[i] = Math.min(newBoard[i], height);
                }

                // check if result is 1.0.0
                if (newBoard[0] == 1 &&
                        newBoard[1] == 0 &&
                        newBoard[2] == 0) {
                    return true;
                }
            }
        }

        return false;
    }
}