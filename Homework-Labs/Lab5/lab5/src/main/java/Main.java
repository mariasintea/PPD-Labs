import multithreading.Multithread;

public class Main {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        /*Sequential solver = new Sequential();
        solver.run();*/
        Multithread solver = new Multithread();
        solver.run();
        long endTime = System.nanoTime();
        System.out.println((double)(endTime - startTime)/1E6);
    }
}
