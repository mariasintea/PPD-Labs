package orderedLinkedList;

import java.util.ArrayList;
import java.util.List;

public class OrderedLinkedList {
    private Node head;

    public OrderedLinkedList(){
        head = null;
    }

    public void add(Monom data){
        Node newNode = new Node(data);
        Node current = head;
        Node previous = null;

        while(current != null && data.getExponent() < current.getInfo().getExponent()){
            previous = current;
            current = current.getNext();
        }

        if(previous == null)
            head = newNode;
        else
            previous.setNext(newNode);

        newNode.setNext(current);
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