import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Math.min;

public class WorkerColumn extends Thread{
    private int N, M, n, m;
    private int start, end;
    private int[][] array, filterArray;
    private Queue<QueueElem> queue;
    private int[][] leftBorder, rightBorder;
    private CyclicBarrier barrier;

    public WorkerColumn(int[][] array, int[][] filterArray, CyclicBarrier barrier, int start, int end){
        this.array = array;
        this.filterArray = filterArray;
        this.N = array.length;
        this.M = array[0].length;
        this.n = filterArray.length;
        this.m = filterArray[0].length;
        this.barrier = barrier;
        this.queue = new LinkedBlockingQueue<>();
        this.leftBorder = new int[N][m/2];
        this.rightBorder = new int[N][m/2];
        this.start = start;
        this.end = end;
    }

    private int transformPixel(int line, int column){
        int s = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++){
                int newLine = line - (i - 1), newColumn = column - (j - 1);
                if (newLine < 0)
                    newLine = 0;
                if (newLine >= N)
                    newLine = N - 1;
                if (newColumn < 0)
                    newColumn = 0;
                if (newColumn >= M)
                    newColumn = M - 1;
                s += array[newLine][newColumn] * filterArray[i][j];
            }
        return s;
    }

    @Override
    public void run() {
        for (int i = 0; i < N; i++)
            for (int j = start; j < start + n / 2; j++)
                leftBorder[i][j - start] = transformPixel(i, j);

        for (int i = 0; i < N; i++)
            for (int j = start + n/2; j < end - n / 2; j++){
                int newValue = transformPixel(i, j);
                queue.add(new QueueElem(i, j, newValue));

                QueueElem peekOfQueue = queue.peek();
                if (peekOfQueue.getLine() > i - n / 2 + 1
                    && peekOfQueue.getColumn() > min(j - m / 2 + 1, start + m / 2 - 1) ){
                    array[peekOfQueue.getLine()][peekOfQueue.getColumn()] = peekOfQueue.getNewValue();
                    queue.poll();
                }
            }

        for (int i = 0; i < N; i++)
            for (int j = end - n/2; j < end; j++)
                rightBorder[i][end - 1 - j] = transformPixel(i, j);

        while(!queue.isEmpty()){
            QueueElem peekOfQueue = queue.peek();
            array[peekOfQueue.getLine()][peekOfQueue.getColumn()] = peekOfQueue.getNewValue();
            queue.poll();
        }

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < N; i++)
            for (int j = start; j < start + n / 2; j++)
                array[i][j] = leftBorder[i][j - start];

        for (int i = 0; i < N; i++)
            for (int j = end - n/2; j < end; j++)
                array[i][j] = rightBorder[i][end - 1 - j];
    }
}