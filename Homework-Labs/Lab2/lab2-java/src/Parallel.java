import java.util.concurrent.CyclicBarrier;

public class Parallel {
    private int nrOfThreads;
    private int[][] array, filterArray;
    final CyclicBarrier barrier;

    public Parallel(int[][] array, int[][] filterArray, int nrOfThreads) {
        this.array = array;
        this.filterArray = filterArray;
        this.nrOfThreads = nrOfThreads;
        barrier = new CyclicBarrier(nrOfThreads);
    }

    public int[][] getArray() {
        return array;
    }

    public void transformMatrix(){
        if (array.length >= array[0].length){
            Thread threads[] = new WorkerLine[nrOfThreads];
            int start = 0;
            int chunk = array.length / nrOfThreads;
            int rest = array.length % nrOfThreads;

            for (int i = 0; i < nrOfThreads; i++) {
                int end = start + chunk;
                if (rest > 0) {
                    end++;
                    rest--;
                }
                threads[i] = new WorkerLine(array, filterArray, barrier, start, end);
                threads[i].start();

                start = end;
            }


            for (int i = 0; i < nrOfThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            Thread threads[] = new WorkerColumn[nrOfThreads];
            int start = 0;
            int chunk = array[0].length / nrOfThreads;
            int rest = array[0].length % nrOfThreads;

            for (int i = 0; i < nrOfThreads; i++) {
                int end = start + chunk;
                if (rest > 0) {
                    end++;
                    rest--;
                }
                threads[i] = new WorkerColumn(array, filterArray, barrier, start, end);
                threads[i].start();

                start = end;
            }


            for (int i = 0; i < nrOfThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
