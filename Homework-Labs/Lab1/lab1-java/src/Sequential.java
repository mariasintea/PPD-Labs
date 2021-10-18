public class Sequential {
    static final int N = 10;
    static final int M = 10;

    int[][] array;
    int[][] filterArray;
    int[][] newArray = new int[N][M];

    public Sequential(int[][] array, int[][] filterArray) {
        this.array = array;
        this.filterArray = filterArray;
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

    public int[][] transformMatrix(){
        for (int i = 0; i < N; i++)
            for (int j = 0; j < M; j++)
                newArray[i][j] = transformPixel(i, j);
        return newArray;
    }
}
