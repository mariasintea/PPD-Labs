package multithreading;

import dataStructures.Monom;
import dataStructures.OrderedLinkedList;

import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WorkerSum extends Thread{
    private Queue<Monom> monoms;
    private OrderedLinkedList result;
    Lock lock = new ReentrantLock();

    public WorkerSum(Queue<Monom> monoms, OrderedLinkedList result) {
        this.monoms = monoms;
        this.result = result;
    }

    private void addMonomToSum(Monom currentMonom){
        boolean found = false;
        for (Monom monom : result.getAll()) {
            if (monom.getExponent() == currentMonom.getExponent()) {
                lock.lock();
                monom.setCoefficient(monom.getCoefficient() + currentMonom.getCoefficient());
                lock.unlock();
                found = true;
            }
        }
        if (!found)
            result.add(currentMonom);
    }

    @Override
    public void run() {
        synchronized (monoms) {
            while (true) {
                try {
                    monoms.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!monoms.isEmpty()) {
                    Monom currentMonom = monoms.peek();
                    addMonomToSum(currentMonom);
                    monoms.poll();
                }
                else
                    break;
            }
        }
    }
}
