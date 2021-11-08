#include "pch.h"

vector<int> Varianta0::readFromFile(string fileName) {
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

void Varianta0::writeToFile(vector<int> nr) {
	ofstream fout("numar3.txt");
	for (int i = 0; i < nr.size(); i++)
		fout << nr[i];
}

Varianta0::Varianta0() {
	startTime = clock();
	nr1 = readFromFile("numar13.txt");
	nr2 = readFromFile("numar23.txt");
	int n = nr1.size() > nr2.size() ? nr1.size() : nr2.size();
	nr3.resize(n);
}

void Varianta0::run() {
	int carry = 0;
	int n = nr1.size() < nr2.size() ? nr1.size() : nr2.size();
	for (int i = 0; i < n; i++) {
		nr3[i] = (nr1[i] + nr2[i] + carry) % 10;
		carry = (nr1[i] + nr2[i] + carry) / 10;
	}

	for (int i = n; i < nr1.size(); i++) {
		nr3[i] = (nr1[i] + carry) % 10;
		carry = (nr1[i] + carry) / 10;
	}

	for (int i = n; i < nr2.size(); i++) {
		nr3[i] = (nr2[i] + carry) % 10;
		carry = (nr2[i] + carry) / 10;
	}

	if (carry > 0)
		nr3.push_back(carry);
	
	writeToFile(nr3);
	endTime = clock();

	cout << (double)(endTime - startTime) / CLOCKS_PER_SEC;
}