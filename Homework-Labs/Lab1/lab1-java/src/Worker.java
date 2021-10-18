public class Worker extends Thread{
    static final int N = 10000;
    static final int M = 10;
    private int threadNo, nrOfThreads;
    private int[][] array, filterArray, newArray;

    public Worker(int threadNo, int nrOfThreads, int[][] array, int[][] filterArray, int[][] newArray){
        this.threadNo = threadNo;
        this.nrOfThreads = nrOfThreads;
        this.array = array;
        this.filterArray = filterArray;
        this.newArray = newArray;
    }

    private int transformPixel(int line, int column){
        int s = 0;
        for (int i = 0; i < filterArray.length; i++)
            for (int j = 0; j < filterArray[0].length; j++){
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

    private void transformLine(int line){
        for (int i = 0; i < M; i++)
            newArray[line][i] = transformPixel(line, i);
    }

    @Override
    public void run() {
        for (int i = threadNo; i < N; i+= nrOfThreads)
            transformLine(i);
    }
}