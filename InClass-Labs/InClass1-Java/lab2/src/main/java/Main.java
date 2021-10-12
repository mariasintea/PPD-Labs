import java.util.Random;

public class Main {
    public static void main(String[] args){
        int n = 10000000;
        double[] a = new double[n];
        double[] b = new double[n];
        double[] c = new double[n];
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            a[i] = r.nextDouble() * 100;
            b[i] = r.nextDouble() * 100;
        }
        //int a[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        //int b[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        //int c[] = new int[10];

        // Secvential
        /*for (int i = 0; i < a.length; i++) {
            c[i] = a[i] + b[i];
        }*/

        //Paralel
        int p = 4;
        System.out.println("H 1");
        Thread t[] = new Worker[p]; // creare array

        int start = 0, end = 0;
        int chunk = a.length/t.length;
        int rest = a.length % t.length;

        long startTime = System.nanoTime();
        for (int i = 0; i < p; i++) {
            end = start + chunk;
            if (rest > 0) {
                end++;
                rest--;
            }
            t[i] = new Worker(i, p, a, b, c, start, end); // creare obiecte Worker
            t[i].start(); // creare threaduri
            // t[i].join(); -> NU pentru ca inseriaza threadurile
            start = end;
        }


        for (int i = 0; i < p; i++) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime();

        // Thread t = new Worker();
        // t.run(); -> NU pentru ca se executa cod secvential
        // t.start();
        // t.join();
        System.out.println("H 2");

        /*for (int i = 0; i < c.length; i++) {
            System.out.println(c[i]);
        }*/

        System.out.println((double)(endTime - startTime)/1E6);
    }
}
