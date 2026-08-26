import java.io.*;

public class floyd {

    // number of vertices in the graph
    private int n;

    // D matrix to record the shortest distances
    private int[][] D;

    // P matrix to record the intermediate vertex
    private int[][] P;

    public floyd() {
    }

    // initialize matrices for a new problem
    public void initialize(int n, int[][] G) {
        this.n = n;
        this.D = new int[n][n];
        this.P = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.D[i][j] = G[i][j];
                this.P[i][j] = 0;
            }
        }
    }

    // Floyd's algorithm
    public void floydAlgo() {
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int newVal = D[i][k] + D[k][j];
                    if (newVal < D[i][j]) {
                        D[i][j] = newVal;
                        P[i][j] = k + 1;   // store vertex number starting from 1
                    }
                }
            }
        }
    }

    // return P matrix as a string
    public String getPathMatrix() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(P[i][j]).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // return one shortest path line as a string
    public String getPathString(int v1, int v2) {
        StringBuilder result = new StringBuilder();
        result.append("V").append(v1).append(" ");
        buildPath(v1 - 1, v2 - 1, result);
        result.append("V").append(v2).append(": ").append(D[v1 - 1][v2 - 1]);
        return result.toString();
    }

    // recursively build intermediate vertices
    private void buildPath(int v1, int v2, StringBuilder sb) {
        int intermediate = P[v1][v2];

        if (intermediate == 0) {
            return;
        }

        int k = intermediate - 1;
        buildPath(v1, k, sb);
        sb.append("V").append(intermediate).append(" ");
        buildPath(k, v2, sb);
    }

    // print one problem result to output file
    public void printResult(PrintWriter out, int problemCount) {
        out.println("Problem " + problemCount + ": n = " + n);
        out.println("P matrix:");
        out.print(getPathMatrix());

        for (int i = 1; i <= n; i++) {
            out.println("V" + i + "-Vj: shortest path and length");
            for (int j = 1; j <= n; j++) {
                out.println(getPathString(i, j));
            }
        }
        out.println();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Floyd <graph-file>");
            return;
        }

        floyd proj = new floyd();
        String fileName = args[0];

        try (
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                PrintWriter out = new PrintWriter(new FileWriter("output.txt"))
        ) {
            String line;
            int problemCount = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("Problem")) {
                    problemCount++;
                    int n = extractN(line);
                    int[][] graph = new int[n][n];

                    for (int i = 0; i < n; i++) {
                        line = br.readLine();
                        while (line != null && line.trim().isEmpty()) {
                            line = br.readLine();
                        }

                        String[] parts = line.trim().split("\\s+");
                        for (int j = 0; j < n; j++) {
                            graph[i][j] = Integer.parseInt(parts[j]);
                        }
                    }

                    proj.initialize(n, graph);
                    proj.floydAlgo();
                    proj.printResult(out, problemCount);
                }
            }

            System.out.println("Output written to output.txt");

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    /// Extracts the number of vertices (n) from a line like:
    /// "Problem 1: n = 7"
    private static int extractN(String line) {
        int idx = line.indexOf("=");
        return Integer.parseInt(line.substring(idx + 1).trim());
    }
}