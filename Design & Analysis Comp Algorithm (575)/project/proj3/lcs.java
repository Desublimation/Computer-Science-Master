public class lcs {
    /// char array to store the input string 1
    private char[] s1;
    /// char array to store the input string 2
    private char[] s2;
    /// 2D array to store the longest Common Subsequence length
    private int[][] C;
    /// 2D array to store the recall way of finding the Subsequence
    private char[][] B;

    public lcs(String input1, String input2){
        s1 = input1.toCharArray();
        s2 = input2.toCharArray();
        int len1 = input1.length();
        int len2 = input2.length();
        C = new int[len1+1][len2+1];
        B = new char[len1+1][len2+1];
    }

    /// help method to check if two char is same
    private boolean isSameChar(char a, char b){
        return a == b;
    }

    /// Test method to print the 2-D array of C and B
    /// to see if the search result is correct
    public void printMatrix(){
        System.out.println("C");
        for(int i = 0 ; i<C.length;i++){
            for(int j = 0 ; j < C[0].length; j++){
                System.out.print(C[i][j]+" ");
            }
            System.out.println();
        }

        System.out.println("\nB");
        for(int i = 0 ; i<B.length;i++){
            for(int j = 0 ; j < B[0].length; j++){
                System.out.print(B[i][j]+" ");
            }
            System.out.println();
        }
    }

    /// using the LCS Dynamic Problem to find the
    /// longest Common Subsequence
    /// the longest value is recording in the C,
    /// and the recall direction is recording in the B
    public void LCSAlgo(){
        for (int i = 1 ; i < s1.length+1; i++){
            for (int j = 1; j<s2.length+1; j++){
                if (isSameChar(s1[i-1], s2[j-1])){
                    C[i][j] = C[i - 1][j - 1] + 1;
                    B[i][j] = 'D';

                }else{
//                    C[i][j] =
                    if (C[i][j-1]>C[i-1][j]){
                        C[i][j] = C[i][j-1];
                        B[i][j] = 'L';
                    }else{
                        C[i][j] = C[i-1][j];
                        B[i][j] = 'U';

                    }
                }
            }
        }
    }

    /// driver method to print the longest common subsequence
    /// based on the B matrix, and result is stored in the StringBuilder
    public void buildResult(){
        StringBuilder sb = new StringBuilder();
        searchSubsequence(s1.length, s2.length, sb);

        System.out.println("Length of LCS: " + C[s1.length][s2.length]);
        System.out.println("LCS: " + sb.toString());
    }

    /// recursive method to recursive recall back based on B
    /// to find the subsequence
    public void searchSubsequence(int i, int j, StringBuilder sb){
        if(i == 0||j==0){
            return;
        }

        if(B[i][j] == 'D'){
            char target = s2[j-1];
            sb.insert(0,target);
//            sb.append(s1[i - 1]);
            searchSubsequence(i-1,j-1, sb);
        }else if (B[i][j] == 'U') {
            searchSubsequence(i-1,j, sb);
        }else{
            searchSubsequence(i,j-1, sb);
        }
    }


    public static void main(String[] args){
        if (args.length != 2) {
            System.out.println("Usage: java lcs <input-string1> <input-string2>");
            return;
        }

        String s1 = args[0];
        String s2 = args[1];

//        System.out.println("Input 1: " + s1);
//        System.out.println("Input 2: " + s2);

        lcs proj = new lcs(s1, s2);

        proj.LCSAlgo();
        proj.buildResult();

//        Test
//        String str1 = "abcb";
//        String str2 = "bdca";
//        LCS lcs = new LCS(str1, str2);
//
//        lcs.LCSAlgo();
//        lcs.printMatrix();
//        lcs.buildResult();

    }
}
