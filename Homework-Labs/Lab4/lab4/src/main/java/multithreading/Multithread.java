package multithreading;

import orderedLinkedList.*;

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
            for (Monom monom : result.getAll())
                if (monom.getCoefficient() != 0) {
                    bW.write(monom.getCoefficient() + " " + monom.getExponent());
                    bW.newLine();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int p = 8;
        Thread readerThread = new WorkerReading(monoms);
        readerThread.start();
        try {
            readerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread[] sumThreads = new WorkerSum[p - 1];
        for (int i = 0; i < p - 1; i++) {
            sumThreads[i] = new WorkerSum(monoms, result);
            sumThreads[i].start();
        }
        try {
            for (int i = 0; i < p - 1; i++) {
                sumThreads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writeResult();
    }
}
