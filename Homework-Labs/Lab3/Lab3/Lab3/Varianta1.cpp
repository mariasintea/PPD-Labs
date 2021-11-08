#include "pch.h"

void Varianta1::writeToFile(vector<int> nr) {
	ofstream fout("numar3.txt");
	for (int i = 0; i < nr.size(); i++)
		fout << nr[i];
}

Varianta1::Varianta1() {}

void Varianta1::run() {
	int nrOfProcesses, rank;
	MPI_Status status;
	int startTime, endTime;

	int rc = MPI_Init(NULL, NULL);
	if (rc != MPI_SUCCESS) {
		cout << "MPI initialization error";
		MPI_Abort(MPI_COMM_WORLD, rc);
	}

	MPI_Comm_size(MPI_COMM_WORLD, &nrOfProcesses);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	vector<int> nr1_local;
	vector<int> nr2_local;
	vector<int> nr3_local;
	int nrElems, carry = 0;

	if (rank == 0) {
		startTime = clock();
		int n1, n2, idCurrentProcess = 1;
		ifstream fin1("numar13.txt");
		ifstream fin2("numar23.txt");
		fin1 >> n1;
		fin2 >> n2;
		int n = n1 > n2 ? n1 : n2, nrOfProcessesC = nrOfProcesses - 1;
		if (n % nrOfProcessesC != 0)
			n = n + nrOfProcessesC - n % nrOfProcessesC;
		nrElems = n / nrOfProcessesC;
		while (idCurrentProcess < nrOfProcesses) {
			nr1_local.clear();
			nr2_local.clear();
			char c;
			for (int i = 0; i < nrElems; i++)
			{
				if (n1 > 0) {
					fin1 >> c;
					nr1_local.push_back(c - '0');
					n1--;
				}
				else
					nr1_local.push_back(0);
				if (n2 > 0) {
					fin2 >> c;
					nr2_local.push_back(c - '0');
					n2--;
				}
				else
					nr2_local.push_back(0);
			}
			MPI_Send(nr1_local.data(), nr1_local.size(), MPI_INT, idCurrentProcess, 0, MPI_COMM_WORLD);
			MPI_Send(nr2_local.data(), nr2_local.size(), MPI_INT, idCurrentProcess, 0, MPI_COMM_WORLD);
			idCurrentProcess++;
		}
	}
	MPI_Bcast(&nrElems, 1, MPI_INT, 0, MPI_COMM_WORLD);
	nr1_local.resize(nrElems);
	nr2_local.resize(nrElems);
	nr3_local.resize(nrElems);

	if (rank != 0) {
		MPI_Recv(nr1_local.data(), nr1_local.size(), MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
		MPI_Recv(nr2_local.data(), nr2_local.size(), MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
		if (rank > 1)
			MPI_Recv(&carry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, &status);

		for (int i = 0; i < nrElems; i++) {
			nr3_local[i] = (nr1_local[i] + nr2_local[i] + carry) % 10;
			carry = (nr1_local[i] + nr2_local[i] + carry) / 10;
		}

		if (rank != nrOfProcesses - 1)
			MPI_Send(&carry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
		else
			MPI_Send(&carry, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);

		MPI_Send(nr3_local.data(), nr3_local.size(), MPI_INT, 0, 0, MPI_COMM_WORLD);
	}
	else {
		MPI_Recv(&carry, 1, MPI_INT, nrOfProcesses - 1, 0, MPI_COMM_WORLD, &status);
		for (int i = 1; i < nrOfProcesses; i++) {
			MPI_Recv(nr3_local.data(), nr3_local.size(), MPI_INT, i, 0, MPI_COMM_WORLD, &status);
			for (auto digit : nr3_local)
				nr3.push_back(digit);
		}
		if (carry > 0)
			nr3.push_back(carry);
		writeToFile(nr3);
		endTime = clock();

		cout << (double)(endTime - startTime) / CLOCKS_PER_SEC;
	}

	MPI_Finalize();
}