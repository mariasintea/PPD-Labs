#include "Dynamic.h"
#define N 10
#define M 10
#define n 3
#define m 3
#define NR_THRS 2

using namespace std;

const string fileName = "in1.txt";
Barrier barrierD(NR_THRS);

struct QueueElem{
    int line, column, value;
    QueueElem(int l, int c, int v){
        line = l;
        column = c;
        value = v;
    }
};

int transformPixel(int line, int column, int **array, int **filterArray){
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

void workerLine(int **array, int **filterArray, int start, int end) {
    int** upperBorder = new int*[n/2];
    for (int i = 0; i < n/2; ++i)
        upperBorder[i] = new int[M];
    int** lowerBorder = new int*[n/2];
    for (int i = 0; i < n/2; ++i)
        lowerBorder[i] = new int[M];
    queue<QueueElem> queue;

    for (int i = start; i < start + n / 2; i++)
        for (int j = 0; j < M; j++)
            upperBorder[i - start][j] = transformPixel(i, j, array, filterArray);

    for (int i = start + n/2; i < end - n / 2; i++)
        for (int j = 0; j < M; j++){
            int newValue = transformPixel(i, j, array, filterArray);
            queue.push(QueueElem(i, j, newValue));

            QueueElem peekOfQueue = queue.front();
            if (peekOfQueue.line > min(i - n / 2 + 1, start + n / 2 - 1)
                && peekOfQueue.column > j - m / 2 + 1){
                array[peekOfQueue.line][peekOfQueue.column] = peekOfQueue.value;
                queue.pop();
            }
        }

    for (int i = end - n/2; i < end; i++)
        for (int j = 0; j < M; j++)
            lowerBorder[end - 1 - i][j] = transformPixel(i, j, array, filterArray);

    while(!queue.empty()){
        QueueElem peekOfQueue = queue.front();
        array[peekOfQueue.line][peekOfQueue.column] = peekOfQueue.value;
        queue.pop();
    }

    barrierD.wait();

    for (int i = start; i < start + n / 2; i++)
        for (int j = 0; j < M; j++)
            array[i][j] = upperBorder[i - start][j];

    for (int i = end - n/2; i < end; i++)
        for (int j = 0; j < M; j++)
            array[i][j] = lowerBorder[end - 1 - i][j];
}

void workerColumn(int **array, int **filterArray, int start, int end){
    int** upperBorder = new int*[N];
    for (int i = 0; i < N; ++i)
        upperBorder[i] = new int[m/2];
    int** lowerBorder = new int*[N];
    for (int i = 0; i < N; ++i)
        lowerBorder[i] = new int[m/2];
    queue<QueueElem> queue;

    for (int i = 0; i < N; i++)
        for (int j = start; j < start + n / 2; j++)
            upperBorder[i][j - start] = transformPixel(i, j, array, filterArray);

    for (int i = 0; i < N; i++)
        for (int j = start + n/2; j < end - n / 2; j++){
            int newValue = transformPixel(i, j, array, filterArray);
            queue.push(QueueElem(i, j, newValue));

            QueueElem peekOfQueue = queue.front();
            if (peekOfQueue.line > i - n / 2 + 1 && peekOfQueue.column > min(j - m / 2 + 1, start + m / 2 - 1) ){
                array[peekOfQueue.line][peekOfQueue.column] = peekOfQueue.value;
                queue.pop();
            }
        }

    for (int i = 0; i < N; i++)
        for (int j = end - n/2; j < end; j++)
            lowerBorder[i][end - 1 - j] = transformPixel(i, j, array, filterArray);

    while(!queue.empty()){
        QueueElem peekOfQueue = queue.front();
        array[peekOfQueue.line][peekOfQueue.column] = peekOfQueue.value;
        queue.pop();
    }

    barrierD.wait();

    for (int i = 0; i < N; i++)
        for (int j = start; j < start + n / 2; j++)
            array[i][j] = upperBorder[i][j - start];

    for (int i = 0; i < N; i++)
        for (int j = end - n/2; j < end; j++)
            array[i][j] = lowerBorder[i][end - 1 - j];
}

void transformMatrix(int **array, int **filterArray){
    int nrOfThreads = NR_THRS;
    vector<thread> t;
    if (N >= M){
        int start = 0;
        int chunk = N / NR_THRS;
        int rest = N % NR_THRS;
        for (int i = 0; i < nrOfThreads; i++) {
            int end = start + chunk;
            if (rest > 0) {
                end++;
                rest--;
            }
            thread thr = thread(workerLine, array, filterArray, start, end);
            t.push_back(move(thr));
            start = end;
        }

        for (int i = 0; i < t.size(); i++) {
            if (t[i].joinable())
                t[i].join();
        }
    }
    else
    {
        int start = 0;
        int chunk = M / NR_THRS;
        int rest = M % NR_THRS;
        for (int i = 0; i < nrOfThreads; i++) {
            int end = start + chunk;
            if (rest > 0) {
                end++;
                rest--;
            }
            thread thr = thread(workerColumn, array, filterArray, start, end);
            t.push_back(move(thr));
            start = end;
        }

        for (int i = 0; i < t.size(); i++) {
            if (t[i].joinable())
                t[i].join();
        }
    }
}

void readData(int **array, int **filterArray){
    ifstream fin(fileName);

    for (int i = 0; i < N; i++)
        for (int j = 0; j < M; j++)
            fin >> array[i][j];

    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            fin >> filterArray[i][j];
}

void runParallelDynamic(){
    int** array = new int*[N];
    for (int i = 0; i < N; ++i)
        array[i] = new int[M];
    int** filterArray = new int*[n];
    for (int i = 0; i < n; ++i)
        filterArray[i] = new int[m];

    readData(array, filterArray);

    clock_t startTime = clock();
    transformMatrix(array, filterArray);
    clock_t endTime = clock();

    cout << (double)(endTime - startTime) / CLOCKS_PER_SEC;
    /*for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++)
            cout << array[i][j] << " ";
        cout << '\n';
    }*/
}