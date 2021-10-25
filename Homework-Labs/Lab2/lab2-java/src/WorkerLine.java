import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Math.min;

public class WorkerLine extends Thread{
    private int N, M, n, m;
    private int start, end;
    private int[][] array, filterArray;
    private Queue<QueueElem> queue;
    private int[][] upperBorder, lowerBorder;
    private CyclicBarrier barrier;

    public WorkerLine(int[][] array, int[][] filterArray, CyclicBarrier barrier, int start, int end){
        this.array = array;
        this.filterArray = filterArray;
        this.N = array.length;
        this.M = array[0].length;
        this.n = filterArray.length;
        this.m = filterArray[0].length;
        this.barrier = barrier;
        this.queue = new LinkedBlockingQueue<>();
        this.upperBorder = new int[n/2][M];
        this.lowerBorder = new int[n/2][M];
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
        for (int i = start; i < start + n / 2; i++)
            for (int j = 0; j < M; j++)
                upperBorder[i - start][j] = transformPixel(i, j);

        for (int i = start + n/2; i < end - n / 2; i++)
            for (int j = 0; j < M; j++){
                int newValue = transformPixel(i, j);
                queue.add(new QueueElem(i, j, newValue));

                QueueElem peekOfQueue = queue.peek();
                if (peekOfQueue.getLine() > min(i - n / 2 + 1, start + n / 2 - 1)
                        && peekOfQueue.getColumn() > j - m / 2 + 1){
                    array[peekOfQueue.getLine()][peekOfQueue.getColumn()] = peekOfQueue.getNewValue();
                    queue.poll();
                }
            }

        for (int i = end - n/2; i < end; i++)
            for (int j = 0; j < M; j++)
                lowerBorder[end - 1 - i][j] = transformPixel(i, j);

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

        for (int i = start; i < start + n / 2; i++)
            for (int j = 0; j < M; j++)
                array[i][j] = upperBorder[i - start][j];

        for (int i = end - n/2; i < end; i++)
            for (int j = 0; j < M; j++)
                array[i][j] = lowerBorder[end - 1 - i][j];
    }
}