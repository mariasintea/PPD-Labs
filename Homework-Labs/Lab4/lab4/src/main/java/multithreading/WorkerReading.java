package multithreading;

import orderedLinkedList.Monom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class WorkerReading extends Thread{
    private Queue<Monom> monoms;

    public WorkerReading(Queue<Monom> monoms) {
        this.monoms = monoms;
    }

    @Override
    public void run() {
        String fileName = "data/test2/polinom";
        for (int i = 1; i <= 5; i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName + i + ".txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    List<String> nrs = Arrays.asList(line.split(" "));
                    Monom monom = new Monom(Integer.parseInt(nrs.get(0)), Integer.parseInt(nrs.get(1)));
                    synchronized (monoms) {
                        monoms.add(monom);
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
