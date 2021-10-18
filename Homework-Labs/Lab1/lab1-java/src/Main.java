import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

    static final int N = 10000;
    static final int M = 10;
    static final int n = 5;
    static final int m = 5;
    static final String fileName = "in4.txt";

    private static void readData(int[][] array, int[][] filterArray){
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            for (int i = 0; i < N; i++) {
                line = br.readLine();
                List<String> nrs = Arrays.asList(line.split(" "));
                for (int j = 0; j < M; j++)
                    array[i][j] = Integer.parseInt(nrs.get(j));
            }

            for (int i = 0; i < n; i++) {
                line = br.readLine();
                List<String> nrs = Arrays.asList(line.split(" "));
                for (int j = 0; j < m; j++)
                    filterArray[i][j] = Integer.parseInt(nrs.get(j));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*GenerateData generator = new GenerateData(N, M, n, m);
        generator.generate();*/

        int[][] array = new int[N][M];
        int[][] filterArray = new int[n][m];

        readData(array, filterArray);

        /*Sequential seq = new Sequential(array, filterArray);
        long startTime = System.nanoTime();
        int[][] newArray = seq.transformMatrix();
        long endTime = System.nanoTime();*/

        Parallel par = new Parallel(array, filterArray);
        long startTime = System.nanoTime();
        int[][] newArray = par.transformMatrix();
        long endTime = System.nanoTime();

        System.out.println((double)(endTime - startTime)/1E6);
        /*for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                System.out.print(newArray[i][j] + " ");
            }
            System.out.println();
        }*/
    }
}
