import java.io.FileWriter;
import java.util.*;
import java.io.*;
public class knapsack {
    /// capacity of the knapsack
    private int c;

    /// number of total items
    private int n;

    /// list to store all the items of the 01 knapsack problem
    /// where index 0 of element represent profit, index 1 of element represent weight
    private List<int[]> items;

    /// constructor of initialize the instance
    public knapsack() {
        this.c = 0;
        this.n = 0;
        this.items = new ArrayList<>();
    }

    /// randomly generate a 01 knapsack problem and write it in the file with
    /// the given name "filename"
    public void getKnapsackProb(String filename) {
        Random rd = new Random();
        int n = rd.nextInt(6)+5;
        List<int[]> items = new ArrayList<>();
        int totalWeight = 0;
        for(int i = 0; i<n; i++){
            int profit = rd.nextInt(21)+10;
            int weight = rd.nextInt(16)+5;
            int[] item = new int[]{profit, weight};
            items.add(item);
            totalWeight+=weight;
        }
        int capacity = (int)(0.6*totalWeight);

        try{
            FileWriter fw = new FileWriter(filename);
            fw.write(n+" "+capacity+"\n");
            for(int i = 0 ; i < n; i++){
                int[] item = items.get(i);
                fw.write("Item"+(i+1)+" "+item[0]+" "+item[1]+"\n");
            }
            fw.close();
            System.out.println("write success and finished");
        }catch (IOException e){
            System.out.println("got an error for writing knapsack problem ");
            e.printStackTrace();
        }

    }

    /// help method to read the file which records the information
    /// of 01 knapsack problem from the file with the given name "filename"
    /// and store those information into the global instances
    private void readfile(String filename){
        StringBuilder sb = new StringBuilder();
        this.items.clear();
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line = br.readLine();
            if(line != null){
                String[] firstline = line.split("\\s+");
                int num = Integer.parseInt(firstline[0]);
                int capacity = Integer.parseInt(firstline[1]);
                this.n = num;
                this.c = capacity;
            }
            line = br.readLine();
            while(line != null){
                String[] currLine = line.split("\\s+");
                int profit = Integer.parseInt(currLine[1]);
                int weight = Integer.parseInt(currLine[2]);
                int newItem[] = new int[]{profit, weight};
                this.items.add(newItem);
                line = br.readLine();
            }

        }catch(IOException e){
            System.out.println("got an error for reading files");
            e.printStackTrace();
        }
    }

    /// help method to write the solution given by the resultItems which stored which item should be selected in
    /// global list "result" into the file defined by the given name "filename"
    private void writeResult(int num, int maxP, int totalW, List<Integer> resultItems, String filename){
        try{
            FileWriter fw = new FileWriter(filename);
            fw.write(num+" "+maxP+" "+totalW+"\n");
            for(int i = 0 ; i < resultItems.size(); i++){
                int itemIndex = resultItems.get(i);
                int[] item = this.items.get(itemIndex);
                fw.write("Item"+(itemIndex+1)+" "+item[0]+" "+item[1]+"\n");
            }
            fw.close();
            System.out.println("write success and finished");
        }catch (IOException e){
            System.out.println("got an error for writing knapsack problem ");
            e.printStackTrace();
        }
    }

    /// print all information read from the file and stored them into global instances
    /// this method helps me to check if the readfile works well
    public void printKnapsackProblem(){
        System.out.println("items: "+this.n+"; capacity: "+this.c);
        System.out.println("      profit     weight");
        for(int i = 0; i<this.items.size(); i++){
            int[] item = items.get(i);
            System.out.println("item"+(i+1)+" "+item[0]+"         "+item[1]);
        }
    }

    /// brute force the 01 knapsack problem and find the best solution
    public void bruteForceKnapsack(String filename){
        readfile(filename);

        int totoalCombin = 1 << this.n; // 2^n

        int bestProfit = 0;
        int bestMask = 0;
        int bestWeight = 0;
        for(int mask = 0 ; mask < totoalCombin; mask++){

            int currProfit = 0;
            int currWeight = 0;

            for(int i = 0; i<this.n; i++){
                if((mask&(1<<i))!=0){
                    int[] item = this.items.get(i);
                    currProfit += item[0];
                    currWeight += item[1];
                }
            }

            if(currWeight<=this.c && currProfit>bestProfit){
                bestWeight = currWeight;
                bestProfit = currProfit;
                bestMask = mask;
            }
        }

        List<Integer> result = new ArrayList<>();
        for(int i = 0; i<this.n; i++){
            if((bestMask&(1<<i))!=0){
                result.add(i);
            }
        }
        writeResult(result.size(), bestProfit, bestWeight, result, "output1.txt");

    }

    /// using refinement Dynamic programming method to find the best solution
    /// of 01 knapsack problem
    public void dpKnapsack(String filename){
        readfile(filename);

        int[][] B = new int[this.n+1][this.c+1];
        List<TreeSet<Integer>> needed = new ArrayList<>();
        List<List<Integer>> result = new ArrayList<>();


        // initialize: finding those who needed
        for (int i = 0 ; i<=this.n; i++){
            needed.add(new TreeSet<>());

            if(i>0){
                result.add(new ArrayList<>());
            }
        }

        needed.get(this.n).add(this.c);
        for (int i = this.n; i>1; i--){
            TreeSet<Integer> currRow = needed.get(i);
            TreeSet<Integer> nextRow = needed.get(i-1);

            for (int w: currRow ){
                nextRow.add(w);

                int[] curritem = this.items.get(i-1);
                int nextW = w-curritem[1];
                if(nextW>=0)
                    nextRow.add(nextW);
            }
        }


        // iteration those entries that is needed and calculate values
        for (int i  = 1; i<needed.size(); i++){
            for(int w: needed.get(i)){
                int[] currItem = this.items.get(i-1);

                int row = i;
                int col = w;
                int offset = col-currItem[1];
                if(offset<0){
                    B[row][col] = B[row-1][col];

                    result.get(i-1).add( B[row-1][col]);
                }else{
                    int include = B[row-1][offset]+currItem[0];
                    int notInclude = B[row-1][col];
                    int maxP = Math.max(include, notInclude);
                    B[row][col] = maxP;

                    result.get(i-1).add(maxP);
                }

            }
        }
        // write the output file;
        getDpCombination(B);

        // write the refinement matrix into the file;
        List<String> strMatrix = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            sb.append("row"+(i+1));
            for (int p : result.get(i)) {
                sb.append("\t"+p);
            }
            sb.append("\n");
            strMatrix.add(sb.toString());
            sb.setLength(0);
        }
        try{
            FileWriter fw = new FileWriter("entries2.txt");
            for(int i = 0 ; i < strMatrix.size(); i++){
                fw.write(strMatrix.get(i));
            }
            fw.close();
            System.out.println("write success and finished");
        }catch (IOException e){
            System.out.println("got an error for writing knapsack problem ");
            e.printStackTrace();
        }

    }

    /// help method to backtrack which item is needed
    private void getDpCombination(int[][] B){
        int maxP = B[this.n][this.c];
        int totalW = 0;
        int w = this.c;

        List<Integer> result = new ArrayList<>();

        for(int i = this.n; i > 0 && w > 0; i--){
            if(B[i][w] != B[i-1][w]){
                int[] item = this.items.get(i-1);
                result.add(i-1);
                totalW += item[1];
                w -= item[1];
            }
        }

        Collections.reverse(result);
        writeResult(result.size(), maxP, totalW, result, "output2.txt");
    }

    /// use the greedy 4 to find the best solution of 01 knapsack problem
    public void greedyKnapsack(String filename){
        readfile(filename);

        //greedy pick the highest unit price item;
        List<Item> sortedItems = new ArrayList<>(); // sort items by unit price and add info into "items" obj, and then add it into the list
        for(int i = 0; i < this.items.size(); i++){
            int profit =items.get(i)[0];
            int weight = items.get(i)[1];


            double p = (double) profit /weight;
            sortedItems.add(new Item(i, profit, weight, p));
        }
        sortedItems.sort(Comparator.comparingDouble((Item item) -> item.unitedPrice).reversed());


//      direct get the single one that has the best profit
        List<Integer> result1 = new ArrayList<>();
        int restCap = this.c;
        int maxp = 0;
        int totalW = 0;
        for(Item item: sortedItems){
            int tempCap = restCap-item.weight;
            if(tempCap>=0){
                maxp += item.profit;
                totalW += item.weight;
                restCap = tempCap;
                result1.add(item.itemNum);
            }
        }
        Collections.sort(result1);

        int maxSingleProfit = 0;
        int maxSingleWeight = 0;
        int maxSingleIndex = -1;

        for (Item item : sortedItems) {
            if (item.weight <= this.c && item.profit > maxSingleProfit) {
                maxSingleProfit = item.profit;
                maxSingleWeight = item.weight;
                maxSingleIndex = item.itemNum;
            }
        }

        if(maxSingleProfit <=maxp){
            writeResult(result1.size(),maxp,totalW,result1,"output3.txt");
        }else{
            List<Integer> result2 = new ArrayList<>();
            result2.add(maxSingleIndex);
            writeResult(1, maxSingleProfit, maxSingleWeight, result2, "output3.txt");
        }
    }

    /// nest class for the greedy algorithm which helps
    /// items list can be sorted based on united price while can
    /// remember the item sequence number
    private class Item{
        int itemNum;
        int profit;
        int weight;
        double unitedPrice;
        Item(int itemNum, int profit, int weight, double unitedPrice) {
            this.itemNum = itemNum;
            this.profit = profit;
            this.weight = weight;
            this.unitedPrice = unitedPrice;
        }

        /// show the item info
        public String toString(){

            return "index: "+itemNum+"; profit: "+profit+"; weight: "+weight+"; united price"+unitedPrice;
        }
    }


    public static void main(String[] args){
        knapsack kn = new knapsack();

//        quick check
//        kn.getKnapsackProb();
//        kn.bruteForceKnapsack("knapsack01.txt");
//        kn.dpKnapsack("knapsack01.txt");
//        kn.greedyKnapsack("knapsack01.txt");

        if (args.length < 1) {
            System.out.println("Usage:");
            System.out.println("java knapsack createkn01 knapsack01.txt");
            System.out.println("java knapsack bruteforce knapsack01.txt");
            System.out.println("java knapsack dynpro knapsack01.txt");
            System.out.println("java knapsack igreedy knapsack01.txt");
            return;
        }

        String command = args[0];
        if (command.equals("createkn01")) {
            String filename = args.length >= 2 ? args[1] : "knapsack01.txt";
            kn.getKnapsackProb(filename);
        }else if (command.equals("bruteforce")) {
            if (args.length < 2) {
                System.out.println("Missing input file.");
                return;
            }
            kn.bruteForceKnapsack(args[1]);
        }else if (command.equals("dynpro")) {
            if (args.length < 2) {
                System.out.println("Missing input file.");
                return;
            }
            kn.dpKnapsack(args[1]);
        }else if (command.equals("igreedy")) {
            if (args.length < 2) {
                System.out.println("Missing input file.");
                return;
            }
            kn.greedyKnapsack(args[1]);
        }
        else {
            System.out.println("Unknown command: " + command);
        }
    }
}
