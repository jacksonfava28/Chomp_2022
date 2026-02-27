public class MyChomp {

    public static void main(String[] args) {

        for (int c1 = 3; c1 >= 0; c1--) {
            for (int c2 = c1; c2 >= 0; c2--) {
                for (int c3 = c2; c3 >= 0; c3--) {

                    // skip 0.0.0
                    if (c1 != 0 || c2 != 0 || c3 != 0) {
                        System.out.println(c1 + "." + c2 + "." + c3);
                    }

                }
            }
        }
//
    }
}