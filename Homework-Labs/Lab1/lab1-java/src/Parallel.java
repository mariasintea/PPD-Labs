public class Parallel {
    static final int N = 10000;
    static final int M = 10;
    int nrOfThreads = 1;

    int[][] array;
    int[][] filterArray;
    int[][] newArray = new int[N][M];

    public Parallel(int[][] array, int[][] filterArray) {
        this.array = array;
        this.filterArray = filterArray;
    }

    public int[][] transformMatrix(){
        Thread threads[] = new Worker[nrOfThreads];

        for (int i = 0; i < nrOfThreads; i++) {
            threads[i] = new Worker(i, nrOfThreads, array, filterArray, newArray);
            threads[i].start();
        }

        for (int i = 0; i < nrOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return newArray;
    }
}
