public class Worker extends Thread{
    private int nr_thread, p, start, end;
    private double[] a;
    private double[] b;
    private double[] c;

    public Worker(int nr_thread, int p, double a[], double b[], double c[], int start, int end){
        this.nr_thread = nr_thread;
        this.p = p;
        this.a = a;
        this.b = b;
        this.c = c;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        //System.out.println(this.getName());

        /*for (int i = nr_thread; i < c.length; i+= p) { // ciclic -varianta 1
            c[i] = a[i] + b[i];
        }*/

        // ciclic -varianta 2
        /*for (int i = 0; i < c.length; i++) {
            if (i % p == nr_thread)
                c[i] = a[i] + b[i];
        }*/

        //liniar
        /*for (int i = start; i < end; i++) {
            c[i] = a[i] + b[i];
        }*/

        for (int i = start; i < end; i++) {
            c[i] = Math.sqrt(Math.pow(a[i], 4) + Math.pow(b[i], 4));
        }
    }
}
