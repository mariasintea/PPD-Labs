#include <iostream>
#include <fstream>
#include "cuda_runtime.h"
#include "device_launch_parameters.h"

#define INF 0x3f3f3f3f
#define THREADS_PER_BLOCK_SIDE 16

using namespace std;

__global__ void calculate(int *input, int *output, int n, int k)
{
    int i = blockDim.y * blockIdx.y + threadIdx.y;
    int j = blockDim.x * blockIdx.x + threadIdx.x;

    if (i < n && j < n){
        if (input[i * n + k] + input[k * n + j] < input[i * n + j])
            input[i * n + j] = input[i * n + k] + input[k * n + j];
        output[i * n + j] = input[i * n + j];
    }
}

void writeToFile(int *output, int n)
{
    ofstream fout("royfloyd.out");
    for(int i = 0; i < n; i++)
    {
        for(int j = 0; j < n; j++)
        {
            if(output[i * n + j] == INF)
                output[i * n + j] = 0;
            fout << output[i * n + j] << " ";
        }
        fout << "\n";
    }
}

int main()
{
    int n;
    int *graphHostInput;
    int *graphHostOutput;
    int *graphDeviceInput;
    int *graphDeviceOutput;

    ifstream fin("royfloyd.in");
    fin >> n;

    graphHostInput = (int*) malloc(sizeof(int) * n * n);

    for(int i = 0;i < n;i++)
        for(int j = 0;j < n;j++)
        {
            fin >> graphHostInput[i * n + j];
            if(i != j && !graphHostInput[i * n + j])
                graphHostInput[i * n + j] = INF;
        }

    graphHostOutput = (int*) malloc(sizeof(int) * n * n);
    cudaMalloc((void**)&graphDeviceInput, sizeof(int) * n * n);
    cudaMalloc((void**)&graphDeviceOutput, sizeof(int) * n * n);

    cudaMemcpy(graphDeviceInput, graphHostInput, n * n * sizeof(int), cudaMemcpyHostToDevice);
    const int BLOCKS_PER_GRAPH_SIDE = ((n + THREADS_PER_BLOCK_SIDE - 1) / THREADS_PER_BLOCK_SIDE);
    dim3 blocks(BLOCKS_PER_GRAPH_SIDE, BLOCKS_PER_GRAPH_SIDE, 1);
    dim3 threadsPerBlock(THREADS_PER_BLOCK_SIDE, THREADS_PER_BLOCK_SIDE, 1);
    for (int k = 0; k < n; k++) {
        calculate<<<blocks, threadsPerBlock>>>(graphDeviceInput, graphDeviceOutput, n, k);
    }
    cudaMemcpy(graphHostOutput, graphDeviceOutput, sizeof(int) * n * n, cudaMemcpyDeviceToHost);

    cudaFree(graphDeviceInput);
    cudaFree(graphDeviceOutput);

    writeToFile(graphHostOutput, n);

    free(graphHostInput);
    free(graphHostOutput);
    return 0;
}