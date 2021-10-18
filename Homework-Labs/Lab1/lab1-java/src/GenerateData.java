import java.util.Random;

public class GenerateData {
    int N, M, n, m;
    Random rand = new Random();

    public GenerateData(int n, int m, int n1, int m1) {
        N = n;
        M = m;
        this.n = n1;
        this.m = m1;
    }

    void generate(){
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                int x = rand.nextInt(100);
                System.out.print(x + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int x = rand.nextInt(100);
                System.out.print(x + " ");
            }
            System.out.println();
        }
    }
}
