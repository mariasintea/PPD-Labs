#include "pch.h"
#include <iostream>
#include <thread>
#include <time.h>
#include <vector>
#include <cstdlib>
#define MAX 1000

using namespace std;

void worker_ciclic(double a[], double b[], double c[], int p, int nr_thr) {
	for (int i = nr_thr; i < MAX; i += p) {
		c[i] = a[i] + b[i];
	}
}

void worker_liniar(double a[], double b[], double c[], int start, int end) {
	for (int i = start; i < end; i++) {
		c[i] = a[i] + b[i];
	}
}

int main(int argc, char** argv)
{
	double a[MAX];
	double b[MAX];
	double c[MAX];

	// double a[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	// double b[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	// double c[MAX];

	srand(time(NULL));
	for (int i = 0; i < MAX; i++) {
		a[i] = rand() % 100 + 1;
		b[i] = rand() % 100 + 1;
	}

	//SECVENTIAL
	/*for (int i = 0; i < MAX; i++) {
		c[i] = a[i] + b[i];
	}*/

	//worker(a, b, c);
	//thread t(worker, a, b, c);

	int p = 4;
	//int p = atoi(argv[1]); - pentru rulare din linia de comanda
	vector<thread> t;

	clock_t startTime = clock();
	
	//CICLIC
	/*for (int i = 0; i < p; i++) {
		thread thr = thread(worker_ciclic, a, b, c, p, i);
		t.push_back(move(thr));
	}*/
	
	//LINIAR
	int start = 0, end, chunk = MAX / p, rest = MAX % p;
	for (int i = 0; i < p; i++) {
		end = start + chunk + (rest-- > 0);
		thread thr = thread(worker_liniar, a, b, c, start, end);
		t.push_back(move(thr));
		start = end;
	}

	for (int i = 0; i < t.size(); i++) {
		if (t[i].joinable())
			t[i].join();
	}
	//t.join();

	clock_t endTime = clock();

	/*for (int i = 0; i < MAX; i++)
	{
		cout << c[i] << " ";
	}*/

	cout << (double)(endTime - startTime) / CLOCKS_PER_SEC; //sec

	return 0;
}

