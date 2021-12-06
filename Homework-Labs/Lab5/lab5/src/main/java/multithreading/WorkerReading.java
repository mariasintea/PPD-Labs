package multithreading;

import dataStructures.Monom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class WorkerReading extends Thread{
    protected Queue<Monom> monoms;
    private int nrOfThreads;
    private int threadId;

    public WorkerReading(Queue<Monom> monoms, int nrOfThreads, int threadId) {
        this.monoms = monoms;
        this.nrOfThreads = nrOfThreads;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        String fileName = "data/test1/polinom";
        final int n = 10;
        for (int i = threadId + 1; i <= n; i+= nrOfThreads) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName + i + ".txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    List<String> nrs = Arrays.asList(line.split(" "));
                    Monom monom = new Monom(Integer.parseInt(nrs.get(0)), Integer.parseInt(nrs.get(1)));
                    synchronized (monoms) {
                        monoms.add(monom);
                        monoms.notify();
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        while(true){
            synchronized (monoms){
                if(!monoms.isEmpty())
                    monoms.notify();
                else
                    break;
            }
        }

        synchronized (monoms){
            monoms.notifyAll();
        }
    }
}
