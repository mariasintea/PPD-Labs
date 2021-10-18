#include "Dynamic.h"
#define N 1000
#define M 1000
#define n 5
#define m 5
#define NR_THRS 1

using namespace std;

const string fileName = "in2.txt";

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

void transformLine(int line, int **array, int **filterArray, int **newArray) {
    for (int i = 0; i < M; i++)
        newArray[line][i] = transformPixel(line, i, array, filterArray);
}

void worker(int threadNo, int nrOfThreads, int **array, int **filterArray, int **newArray) {
    for (int i = threadNo; i < N; i+= nrOfThreads)
        transformLine(i, array, filterArray, newArray);
}

void transformMatrix(int **array, int **filterArray, int **newArray){
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
    int** newArray = new int*[N];
    for (int i = 0; i < N; ++i)
        newArray[i] = new int[M];
    int** filterArray = new int*[n];
    for (int i = 0; i < n; ++i)
        filterArray[i] = new int[m];

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

void runSequentialDynamic(){
    int** array = new int*[N];
    for (int i = 0; i < N; ++i)
        array[i] = new int[M];
    int** newArray = new int*[N];
    for (int i = 0; i < N; ++i)
        newArray[i] = new int[M];
    int** filterArray = new int*[n];
    for (int i = 0; i < n; ++i)
        filterArray[i] = new int[m];

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
