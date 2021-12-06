package multithreading;

import dataStructures.*;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class Multithread {
    private OrderedLinkedList result;
    private Queue<Monom> monoms;

    public Multithread(){
        result = new OrderedLinkedList();
        monoms = new ArrayDeque<>();
    }

    private void writeResult(){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter("data/result.txt", false))) {
            result.deleteZeros();
            for (Monom monom : result.getAll()) {
                bW.write(monom.getCoefficient() + " " + monom.getExponent());
                bW.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int p1 = 3, p2 = 5;
        Thread[] readerThreads = new WorkerReading[p1];
        for (int i = 0; i < p1; i++) {
            readerThreads[i] = new WorkerReading(monoms, p1, i);
            readerThreads[i].start();
        }
        Thread[] sumThreads = new WorkerSum[p2];
        for (int i = 0; i < p2; i++) {
            sumThreads[i] = new WorkerSum(monoms, result);
            sumThreads[i].start();
        }
        try {
            for (int i = 0; i < p1; i++) {
                readerThreads[i].join();
            }
            for (int i = 0; i < p2; i++) {
                sumThreads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writeResult();
    }
}
