package orderedLinkedList;

public class Node {
    private Monom info;
    private Node next;

    public Node(Monom info) {
        this.info = info;
        this.next = null;
    }

    public Monom getInfo() {
        return info;
    }

    public void setInfo(Monom info) {
        this.info = info;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
