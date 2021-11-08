#include "pch.h"
#define min(a, b) a<b?a:b

vector<int> Varianta2::readFromFile(string fileName) {
	vector<int> nr;
	string nrAsString;
	int n;
	ifstream fin(fileName);
	fin >> n;
	fin >> nrAsString;
	for (int i = 0; i < n; i++)
		nr.push_back(nrAsString[i] - '0');
	return nr;
}

void Varianta2::writeToFile(vector<int> nr) {
	ofstream fout("numar3.txt");
	for (int i = 0; i < nr.size(); i++)
		fout << nr[i];
}

Varianta2::Varianta2() {}

void Varianta2::run() {
	int nrOfProcesses, rank, nrElems;
	MPI_Status status;
	int startTime, endTime;

	int rc = MPI_Init(NULL, NULL);
	if (rc != MPI_SUCCESS) {
		cout << "MPI initialization error";
		MPI_Abort(MPI_COMM_WORLD, rc);
	}

	MPI_Comm_size(MPI_COMM_WORLD, &nrOfProcesses);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	if (rank == 0) {
		startTime = clock();
		nr1 = readFromFile("numar13.txt");
		nr2 = readFromFile("numar23.txt");

		if (nr1.size() < nr2.size()) {
			for (int i = nr1.size(); i < nr2.size(); i++)
				nr1.push_back(0);
		}
		else {
			for (int i = nr2.size(); i < nr1.size(); i++)
				nr2.push_back(0);
		}

		if (nr1.size() % nrOfProcesses != 0) {
			int nrOfElemsToAdd = nrOfProcesses - nr1.size() % nrOfProcesses;
			for (int i = 0; i < nrOfElemsToAdd; i++) {
				nr1.push_back(0);
				nr2.push_back(0);
			}
		}
		nr3.resize(nr1.size());

		nrElems = nr1.size() / nrOfProcesses;
	}

	MPI_Bcast(&nrElems, 1, MPI_INT, 0, MPI_COMM_WORLD);
	
	vector<int> nr1_local;
	vector<int> nr2_local;
	vector<int> nr3_local;
	nr1_local.resize(nrElems);
	nr2_local.resize(nrElems);
	nr3_local.resize(nrElems);

	MPI_Scatter(nr1.data(), nrElems, MPI_INT, nr1_local.data(), nrElems, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Scatter(nr2.data(), nrElems, MPI_INT, nr2_local.data(), nrElems, MPI_INT, 0, MPI_COMM_WORLD);

	int carry = 0;
	if (rank != 0)
		MPI_Recv(&carry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, &status);
	for (int i = 0; i < nrElems; i++) {
		nr3_local[i] = (nr1_local[i] + nr2_local[i] + carry) % 10;
		carry = (nr1_local[i] + nr2_local[i] + carry) / 10;
	}

	if (rank != nrOfProcesses - 1)
		MPI_Send(&carry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
	if (rank == nrOfProcesses - 1) 
		MPI_Send(&carry, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
	if (rank == 0)
		MPI_Recv(&carry, 1, MPI_INT, nrOfProcesses - 1, 0, MPI_COMM_WORLD, &status);

	MPI_Gather(nr3_local.data(), nrElems, MPI_INT, nr3.data(), nrElems, MPI_INT, 0, MPI_COMM_WORLD);

	if (rank == 0) {
		if (carry > 0)
			nr3.push_back(carry);
		writeToFile(nr3);
		endTime = clock();

		cout << (double)(endTime - startTime) / CLOCKS_PER_SEC;
	}

	MPI_Finalize();
}