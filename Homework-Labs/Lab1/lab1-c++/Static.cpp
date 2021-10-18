#include "Static.h"
#define N 10000
#define M 10
#define n 5
#define m 5
#define NR_THRS 1

using namespace std;

const string fileName = "in4.txt";

int transformPixel(int line, int column, int array[N][M], int filterArray[n][m]){
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

void transformLine(int line, int array[N][M], int filterArray[n][m], int newArray[N][M]) {
    for (int i = 0; i < M; i++)
        newArray[line][i] = transformPixel(line, i, array, filterArray);
}

void worker(int threadNo, int nrOfThreads, int array[N][M], int filterArray[n][m], int newArray[N][M]) {
    for (int i = threadNo; i < N; i+= nrOfThreads)
        transformLine(i, array, filterArray, newArray);
}

void transformMatrix(int array[N][M], int filterArray[n][m], int newArray[N][M]){
    int nrOfThreads = NR_THRS;
    vector<thread> t;
    for (int i = 0; i < nrOfThreads; i++) {
        thread thr = thread(worker, i, nrOfThreads, array, filterArray, newArray);
        t.push_back(move(thr));
    }

    for (int i = 0; i < t.size(); i++) {
        if (t[i].joinable())
            t[i].join();
    }
}

void readData(int array[N][M], int filterArray[n][m]){
    ifstream fin(fileName);

    for (int i = 0; i < N; i++)
        for (int j = 0; j < M; j++)
            fin >> array[i][j];

    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            fin >> filterArray[i][j];
}

void runParallelStatic(){
    int array[N][M];
    int newArray[N][M];
    int filterArray[n][m];

    readData(array, filterArray);

    clock_t startTime = clock();
    transformMatrix(array, filterArray, newArray);
    clock_t endTime = clock();

    cout << (double)(endTime - startTime) / CLOCKS_PER_SEC;
    /*for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++)
            cout << newArray[i][j] << " ";
        cout << '\n';
    }*/
}

void runSequentialStatic(){
    int array[N][M];
    int newArray[N][M];
    int filterArray[n][m];

    readData(array, filterArray);

    clock_t startTime = clock();
    for (int i = 0; i < N; i++)
        for (int j = 0; j < M; j++)
            newArray[i][j] = transformPixel(i, j, array, filterArray);
    clock_t endTime = clock();

    cout << (double)(endTime - startTime) / CLOCKS_PER_SEC;
    /*for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++)
            cout << newArray[i][j] << " ";
        cout << '\n';
    }*/
}
