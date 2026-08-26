import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class program1 {
    private Map<String, Integer> marketMap;
    private List<Problem> pList;
    public program1(){
        marketMap = new HashMap<>();
        pList = new ArrayList<>();
    }
    private class Problem{
        int weight;
        int nCards;
        boolean hasUnknownCard;
        Map<String, Integer> priceMap;
        public Problem(){
            this.weight = 0;
            this.nCards = 0;
            this.priceMap = new LinkedHashMap<>();
            this.hasUnknownCard = false;
        }

        public Problem(int nCards, int weight){
            this.nCards = nCards;
            this.weight = weight;
            this.priceMap = new LinkedHashMap<>();
            hasUnknownCard = false;
        }

        public int getWeight(){
            return weight;
        }

        public int getnCards(){
            return nCards;
        }

        public Map<String, Integer> getPriceMap(){
            return priceMap;
        }

        public void addInPriceMap(String name, int price){
            priceMap.put(name, price);
        }

        public boolean getHasUnknownCard(){
            return hasUnknownCard;
        }

        public void setHasUnknownCard(boolean b){
            hasUnknownCard = b;
        }


        public String toString(){
            return "n(cards num): "+nCards+"; weight: "+weight+"; cards info: "+priceMap.toString();
        }
    }
    private class Result{
        int n;
        int maxProfit;
        int countSelected;
        double timeSeconds;
        List<String> selectedNames;
        Result(int n, int maxProfit, int countSelected, double timeSeconds, List<String> selectedNames) {
            this.n = n;
            this.maxProfit = maxProfit;
            this.countSelected = countSelected;
            this.timeSeconds = timeSeconds;
            this.selectedNames = selectedNames;
        }
        public void setTimeSeconds(double time){
            this.timeSeconds = time;
        }
    }

    public void readMarket(String marketFile){
        try(BufferedReader br = new BufferedReader(new FileReader(marketFile))){
            String line = br.readLine();

            if(line == null)
                throw new RuntimeException("Empty first line or Empty file exception");
            int m = Integer.parseInt(line.trim());
            for(int i = 0 ; i<m; i++){
                line = br.readLine();
                if(line == null)
                    throw new RuntimeException("Empty first line or Empty file exception");

                String[] parts = line.trim().split("\\s+");
                String name = parts[0];
                int price = Integer.parseInt(parts[1]);
                marketMap.put(name, price);

            }

        }catch(IOException e){
            throw new RuntimeException("Failed to read market file: " + marketFile, e);
        }
    }

    private String nextNonEmpty(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) return line;
        }
        return null;
    }
    public void readPrice(String priceFile){
        try(BufferedReader br = new BufferedReader(new FileReader(priceFile))){
            String line;
            while ((line = nextNonEmpty(br)) != null) {
                String[] first = line.trim().split("\\s+");
                if (first.length < 2) continue;

                int n = Integer.parseInt(first[0]);
                int w = Integer.parseInt(first[1]);
                Problem pb = new Problem(n, w);

                for (int i = 0; i < pb.getnCards(); i++) {
                    line = br.readLine();
                    if (line == null)
                        throw new RuntimeException("Price file ended early");

                    String[] parts = line.trim().split("\\s+");
                    String name = parts[0];
                    int price = Integer.parseInt(parts[1]);
                    pb.addInPriceMap(name, price);

                    // check if the card exists in the market file
                    if (!marketMap.containsKey(name)) {
                        pb.setHasUnknownCard(true);
                    }
                }

                pList.add(pb);

            }
        }catch(IOException e){
            throw new RuntimeException("Failed to read market file: " + priceFile, e);
        }

    }

    //brute force to find the max profit
    private Result findMaxProfit(Problem p){
        // If unknown card exists, skip this problem (but keep others)
        if (p.getHasUnknownCard()) {
            System.err.println("Error: price list contains card(s) not found in market file. Skipping problem: " + p);
            return null;
        }
//      initialization
        int n = p.getnCards();
        int W = p.getWeight();
        int bestProfit = 0;
        long bestMask = 0;
        ArrayList<String> bestCombination = new ArrayList<>();
        long totalSituation = 1L << p.getnCards();                              // 2^n;
        Map<String, Integer> priceMap = p.getPriceMap();
        String[] cards = priceMap.keySet().toArray(new String[p.getnCards()]);  // cards array for each problem
        int[] weight = new int[n];
        int[] profit = new int[n];


        for (int i = 0; i < n; i++) {
            String name = cards[i];
            int buyPrice = p.getPriceMap().get(name);
            int marketValue = marketMap.get(name);
            weight[i] = buyPrice;
            profit[i] = marketValue - buyPrice;
        }

//        start brute force
//        for(Problem p: pList){
        for(long curr = 0; curr<totalSituation; curr++){
            int currWeight = 0;
            int currProfit = 0;

            for(int i = 0; i<n; i++){
                if((curr&(1L<<i))!=0){
                    currWeight+=weight[i];
                    currProfit+=profit[i];
                }
            }
            if (currWeight <= W && currProfit > bestProfit) {
                bestProfit = currProfit;
                bestMask = curr;
            }
        }
        for (int i = 0; i < n; i++) {
            if ((bestMask & (1L << i)) != 0) {
                bestCombination.add(cards[i]);
            }
        }

        return new Result(n, bestProfit, bestCombination.size(), 0.0, bestCombination);
    }
    public void getOutput(String outputFile) {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(outputFile))) {
            for (Problem p : pList) {
                long startTime = System.nanoTime();

                Result r = findMaxProfit(p);

                long endTime = System.nanoTime();
                double time = (endTime - startTime) / 1e9;

                if (r == null) {
                    continue;
                }
                r.setTimeSeconds(time);
                bw.write(r.n + " " + r.maxProfit + " " + r.countSelected + " " +
                        String.format(java.util.Locale.US, "%.6f", r.timeSeconds));
                bw.newLine();
                for (String name : r.selectedNames) {
                    bw.write(name);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write output file: " + outputFile, e);
        }
    }

    public void checkRead(){
        System.out.println("debug: Check if the readMarket func works: " + marketMap.toString());
        System.out.println("---------------------------------------------------------------------\ndebug: check if the readPrice func workds:");
        for(Problem p:pList){
            System.out.println(p.toString());
        }
        System.out.println("-----------------------------------------------------------------------");

    }

    public static void main(String[] args) {
        System.out.println("Homework 1");

        program1 profit1 = new program1();
        HashMap<String, String> fileName = new HashMap<>();
        for (int i = 0; i <= args.length-1; i++) {
            if (args[i].startsWith("-")){
                fileName.put(args[i], args[i+1]);
            }
        }
        profit1.readMarket(fileName.get("-m"));
        profit1.readPrice(fileName.get("-p"));
        profit1.getOutput(fileName.get("-o"));
//        profit1.checkRead();





    }
}