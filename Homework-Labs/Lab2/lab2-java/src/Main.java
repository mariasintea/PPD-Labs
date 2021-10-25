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
    static final int nrOfThreads = 16;
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
        int[][] array = new int[N][M];
        int[][] filterArray = new int[n][m];

        readData(array, filterArray);

        Parallel par = new Parallel(array, filterArray, nrOfThreads);
        long startTime = System.nanoTime();
        par.transformMatrix();
        long endTime = System.nanoTime();

        System.out.println((double)(endTime - startTime)/1E6);
        /*for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                System.out.print(par.getArray()[i][j] + " ");
            }
            System.out.println();
        }*/
    }
}
