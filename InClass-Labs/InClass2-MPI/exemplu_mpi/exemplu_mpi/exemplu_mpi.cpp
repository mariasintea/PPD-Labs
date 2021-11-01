
#include "pch.h"
#include <iostream>
#include "mpi.h"
#include <vector>

using namespace std;

int main()
{
	int nr_procese, rank, buf, b = 5;
	vector<int> nr1_local, nr2_local, res_local;
	MPI_Status stare;
	vector<int> nr1, nr2, rezultat;
	int nr_elem;

	int rc = MPI_Init(NULL, NULL);
	if (rc != MPI_SUCCESS) {
		cout << "eroare la init mpi";
		MPI_Abort(MPI_COMM_WORLD, rc);
	}

	MPI_Comm_size(MPI_COMM_WORLD, &nr_procese);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	//cout << rank << " ";

	/*if (rank == 0) {
		b = 1;
		cout << "Proc" << rank << ":" << b << endl;
	}
	else
	{
		cout << "Proc" << rank << ":" << b << endl;
	}

	MPI_Bcast(&b, 1, MPI_INT, 0, MPI_COMM_WORLD);
	//MPI_Barrier(MPI_COMM_WORLD);

	if (rank == 0) {
		// int a = 4;
		// cout << "Proc " << rank << ";" << a << ";" << b;
		cout << "Proc" << rank << ":" << b << endl;
	}
	else {
		cout << "Proc" << rank << ":" << b << endl;
		// cout << "Proc " << rank << ";" << ";" << b;
	}*/

	if (rank == 0) {
		for (int i = 0; i < 10; i++) {
			nr1.push_back(i);
			nr2.push_back(i + 2);
		}

		int max = nr1.size() >= nr2.size() ? nr1.size() : nr2.size();
		rezultat.resize(max);

		nr_elem = max / nr_procese;

		buf = 10;

		for (int i = 1; i < nr_procese; i++) {
			MPI_Send(&buf, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
		}
	}
	else {
		MPI_Recv(&buf, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &stare);
		cout << buf;
	}

	MPI_Bcast(&nr_elem, 1, MPI_INT, 0, MPI_COMM_WORLD);

	nr1_local.resize(nr_elem);
	nr2_local.resize(nr_elem);
	res_local.resize(nr_elem);
	MPI_Scatter(nr1.data(), nr_elem, MPI_INT, nr1_local.data(), nr_elem, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Scatter(nr2.data(), nr_elem, MPI_INT, nr2_local.data(), nr_elem, MPI_INT, 0, MPI_COMM_WORLD);

	for (int i = 0; i < nr_elem; i++) {
		res_local[i] = nr1_local[i] + nr2_local[i];
	}

	MPI_Gather(res_local.data(), nr_elem, MPI_INT, rezultat.data(), nr_elem, MPI_INT, 0, MPI_COMM_WORLD);

	if (rank == 0) {
		for (int i = 0; i < 10; i++) {
			cout << rezultat[i] << ";";
		}
	}

	MPI_Finalize();

	return 0;
}
