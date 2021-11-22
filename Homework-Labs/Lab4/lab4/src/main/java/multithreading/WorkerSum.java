package multithreading;

import orderedLinkedList.Monom;
import orderedLinkedList.OrderedLinkedList;

import java.util.Queue;

public class WorkerSum extends Thread{
    private Queue<Monom> monoms;
    private OrderedLinkedList result;

    public WorkerSum(Queue<Monom> monoms, OrderedLinkedList result) {
        this.monoms = monoms;
        this.result = result;
    }

    private void addMonomToSum(Monom currentMonom){
        boolean found = false;
        for (Monom monom : result.getAll()) {
            if (monom.getExponent() == currentMonom.getExponent()) {
                monom.setCoefficient(monom.getCoefficient() + currentMonom.getCoefficient());
                found = true;
            }
        }
        if (!found)
            result.add(currentMonom);
    }

    @Override
    public void run() {
        synchronized (monoms) {
            while (!monoms.isEmpty()) {
                Monom currentMonom = monoms.peek();
                synchronized (result) {
                    addMonomToSum(currentMonom);
                }
                monoms.poll();
            }
        }
    }
}
