import java.util.*;

public class program2 {

    private static final String lower_right = "LR";
    private static final String lower_left = "LL";
    private static final String upper_left = "UL";
    private static final String upper_right = "UR";

    private String[][] board;
    private int b_size;

    public program2(int board_size){
        b_size = board_size;
        board = new String[board_size][board_size];
    }

    private void fillTromino(int x, int y, String shape){
        int[] array_pos = this.coordinateToArray(x,y);
        int row = array_pos[0];
        int col = array_pos[1];
        if(board[row][col]==null){
            this.board[row][col] = shape;
        }else{
            System.out.println("Error: board["+row+"]["+col+"]"+" = "+board[row][col]+"; which is not correct");
        }

    }

    /// arithmetic of x-y coordinate to 2D array:
    ///     row: = board_size - (y+1)
    ///     col: = x
    ///
    /// MS: Missing value
    /// return:
    ///     an array with fixed size as 2 that:
    ///     result[0] = row;
    ///     result[1] = col;
    ///
    private int[] coordinateToArray(int x, int y) {
        return new int[]{this.b_size - y - 1, x};
    }

    public void printFilled() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<board.length;i++){
            for (int j = 0; j<board[0].length;j++){
                sb.append(board[i][j]+" ");
            }
            sb.append('\n');
        }
        System.out.println(sb.toString());

    }


    public void init_missing(int x, int y){
        fillTromino(x, y, "MS");
    }

    public void tromino(int x_board, int y_board, int x_missing, int y_missing, int board_size){
        if(board_size==2){
            if (x_missing == x_board && y_missing ==y_board){ //lower_left
                fillTromino(x_board, y_board+1,upper_right);
                fillTromino(x_board+1, y_board+1,upper_right);
                fillTromino(x_board+1, y_board,upper_right);
                return;
            }
            if (x_missing == x_board && y_missing ==y_board+1){ //lower_right
                fillTromino(x_board, y_board,lower_right);
                fillTromino(x_board+1, y_board,lower_right);
                fillTromino(x_board+1, y_board+1,lower_right);
                return;

            }
            if (x_missing == x_board+1 && y_missing ==y_board){ //upper_left
                fillTromino(x_board, y_board,upper_left);
                fillTromino(x_board+1, y_board+1,upper_left);
                fillTromino(x_board, y_board+1,upper_left);
                return;

            }
            if (x_missing == x_board+1 && y_missing ==y_board+1){// lower_left
                fillTromino(x_board, y_board,lower_left);
                fillTromino(x_board+1, y_board,lower_left);
                fillTromino(x_board, y_board+1,lower_left);
                return;

            }
        }

        int half_size = board_size / 2;
        // lower left
        int center_ll_x = x_board+half_size-1;
        int center_ll_y = y_board+half_size-1;
        // upper left
        int center_ul_x = x_board+half_size-1;
        int center_ul_y = y_board+half_size;
        // lower right
        int center_lr_x = x_board+half_size;
        int center_lr_y = y_board+half_size-1;
        // upper right
        int center_ur_x = x_board+half_size;
        int center_ur_y = y_board+half_size;

        if(x_missing>=x_board+half_size) {
            if(y_missing>=y_board + half_size){ // upper right missing

                // fill lower_left tromino
                fillTromino(center_ll_x, center_ll_y, lower_left);
                fillTromino(center_ul_x, center_ul_y, lower_left);
                fillTromino(center_lr_x, center_lr_y, lower_left);

                tromino( x_board, y_board, center_ll_x, center_ll_y, half_size );// lower left
                tromino( x_board, y_board + half_size, center_ul_x, center_ul_y, half_size ); // upper left
                tromino( x_board + half_size, y_board, center_lr_x, center_lr_y, half_size ); // lower right
                tromino( x_board + half_size, y_board + half_size, x_missing, y_missing, half_size ); //upper right
            }else{ // lower right missing

                // fill upper left tromino
                fillTromino(center_ll_x, center_ll_y, upper_left);
                fillTromino(center_ul_x, center_ul_y, upper_left);
                fillTromino(center_ur_x, center_ur_y, upper_left);

                tromino( x_board, y_board, center_ll_x, center_ll_y, half_size ); // lower left
                tromino( x_board, y_board + half_size, center_ul_x, center_ul_y, half_size ); // upper left
                tromino( x_board + half_size, y_board, x_missing, y_missing, half_size ); // lower right
                tromino( x_board + half_size, y_board + half_size, center_ur_x, center_ur_y, half_size ); // upper right

            }

        }else{
            if(y_missing>=y_board + half_size){ // upper left missing

                fillTromino(center_ll_x, center_ll_y, lower_right);
                fillTromino(center_lr_x, center_lr_y, lower_right);
                fillTromino(center_ur_x, center_ur_y, lower_right);

                tromino( x_board, y_board, center_ll_x, center_ll_y, half_size ); // lower left
                tromino( x_board, y_board + half_size, x_missing, y_missing, half_size ); // upper left
                tromino( x_board + half_size, y_board, center_lr_x, center_lr_y, half_size ); // lower right
                tromino( x_board + half_size, y_board + half_size, center_ur_x, center_ur_y, half_size ); // upper right

            }else{ // lower left missing

                fillTromino(center_ul_x, center_ul_y, upper_right);
                fillTromino(center_lr_x, center_lr_y, upper_right);
                fillTromino(center_ur_x, center_ur_y, upper_right);

                tromino( x_board, y_board, x_missing, y_missing, half_size ); // lower left
                tromino( x_board, y_board + half_size, center_ul_x, center_ul_y, half_size ); // upper left
                tromino( x_board + half_size, y_board, center_lr_x, center_lr_y, half_size ); // lower right
                tromino( x_board + half_size, y_board + half_size, center_ur_x, center_ur_y, half_size ); // upper right

            }
        }
    }
    public static boolean isPowerOfTwo(int num){
        return num>1 && (num & (num-1)) == 0;
    }

    public static void main(String[] args){
        String line1 = "Please enter size of board as a power of 2 (0 to quit):";
        String issue = "The board size should be a power of 2";
        String line2 = "Please enter coordinates of missing square (separate by a space):";

        int size = -1;
        int x = -1;
        int y = -1;

//        Get input from user;
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println(line1);
            size = sc.nextInt();
            if(size == 0){
                break;
            }

            if (program2.isPowerOfTwo(size)){
                System.out.println(line2);
                sc.nextLine(); // 吃掉前一个 nextInt() 留下的换行

                String coordLine = sc.nextLine().trim();
                String[] parts = coordLine.split("\\s+");

                if (parts.length != 2) {
                    System.out.println("Invalid input. Please enter exactly two integers separated by space.");
                    continue;
                }

                try {
                    x = Integer.parseInt(parts[0]);
                    y = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Coordinates must be integers.");
                    continue;
                }

                if (x < 0 || x >= size || y < 0 || y >= size) {
                    System.out.println("Invalid missing square coordinates.");
                    continue;
                }

                program2 pg = new program2(size);
                pg.init_missing(x,y);
                pg.tromino(0,0,x,y,size);
                pg.printFilled();
            }else{
                System.out.println(issue);
            }

        }
        sc.close();



//        // test case size 2:
//        System.out.println("test tromino size is 2:");
//        System.out.println("missing value is (0,0)");
//        program2 power_of_two1 = new program2(2);
//        power_of_two1.init_missing(0,0);
//        power_of_two1.tromino(0,0,0,0,2);
//        power_of_two1.printFilled();
//
//        System.out.println("missing value is (0,1)");
//        program2 power_of_two2 = new program2(2);
//        power_of_two2.init_missing(0,1);
//        power_of_two2.tromino(0,0,0,1,2);
//        power_of_two2.printFilled();
//
//        System.out.println("missing value is (1,0)");
//        program2 power_of_two3 = new program2(2);
//        power_of_two3.init_missing(1,0);
//        power_of_two3.tromino(0,0,1,0,2);
//        power_of_two3.printFilled();
//
//        System.out.println("missing value is (1,1)");
//        program2 power_of_two4 = new program2(2);
//        power_of_two4.init_missing(1,1);
//        power_of_two4.tromino(0,0,1,1,2);
//        power_of_two4.printFilled();
//        System.out.println();
//
//        // test case size 4:
//        System.out.println("test tromino size is 4:");
//        System.out.println("missing value is (0,0)");
//        program2 power_of_four1 = new program2(4);
//        power_of_four1.init_missing(0,0);
//        power_of_four1.tromino(0,0,0,0,4);
//        power_of_four1.printFilled();
//
//        System.out.println("missing value is (0,3)");
//        program2 power_of_four2 = new program2(4);
//        power_of_four2.init_missing(0,3);
//        power_of_four2.tromino(0,0,0,3,4);
//        power_of_four2.printFilled();
//
//        System.out.println("missing value is (3,0)");
//        program2 power_of_four3 = new program2(4);
//        power_of_four3.init_missing(3,0);
//        power_of_four3.tromino(0,0,3,0,4);
//        power_of_four3.printFilled();
//
//        System.out.println("missing value is (3,3)");
//        program2 power_of_four4 = new program2(4);
//        power_of_four4.init_missing(3,3);
//        power_of_four4.tromino(0,0,3,3,4);
//        power_of_four4.printFilled();
//
//        System.out.println("missing value is (1,1)");
//        program2 power_of_four5 = new program2(4);
//        power_of_four5.init_missing(1,1);
//        power_of_four5.tromino(0,0,1,1,4);
//        power_of_four5.printFilled();
    }
}
