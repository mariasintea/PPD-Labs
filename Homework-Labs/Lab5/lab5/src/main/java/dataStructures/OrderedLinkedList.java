package dataStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderedLinkedList {
    private Node head;
    Lock lockPrevious = new ReentrantLock();
    Lock lockCurrent = new ReentrantLock();

    public OrderedLinkedList(){
        head = null;
    }

    public void add(Monom data){
        Node newNode = new Node(data);
        Node current = head;
        Node previous = null;

        while(current != null && data.getExponent() < current.getInfo().getExponent()){
            lockPrevious.lock();
            previous = current;
            lockPrevious.unlock();

            lockCurrent.lock();
            current = current.getNext();
            lockCurrent.unlock();
        }

        if(previous == null)
            head = newNode;
        else {
            lockPrevious.lock();
            previous.setNext(newNode);
            lockPrevious.unlock();
        }

        newNode.setNext(current);
    }

    public void deleteZeros(){
        Node current = head;
        Node previous = null;
        while (current != null) {
            if (current.getInfo().getCoefficient() == 0){
                if (previous != null)
                    previous.setNext(current.getNext());
                current = current.getNext();
            }
            else {
                previous = current;
                current = current.getNext();
            }
        }
    }

    public List<Monom> getAll() {
        List<Monom> list = new ArrayList<>();
        Node current = head;
        while(current != null){
            list.add(current.getInfo());
            current = current.getNext();
        }
        return list;
    }
}