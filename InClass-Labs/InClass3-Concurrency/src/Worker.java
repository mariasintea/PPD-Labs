public class Worker extends Thread {
    Cont c1;

    Worker(Cont c){
        this.c1 = c;
    }

    @Override
    public void run() {
        synchronized (c1){
            c1.add();
        }

        /*c1.add();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        c1.decrease();
    }
}