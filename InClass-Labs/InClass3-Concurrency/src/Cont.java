import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cont {
    private int sold = 0;
    private int soldEur = 0;
    Lock lock = new ReentrantLock();
    Lock lock2 = new ReentrantLock(); // daca ar fi cu sold euro

    Object lR = new Object(); // cu - synchronized (lR)

    Cont(int sold){
        this.sold = sold;
    }

    /*synchronized*/ void add(){
       //lock.lock();
        for (int i = 0; i < 1000000; i++) {
            //sold += 1;
            sold ++;
        }
        //lock.unlock();
    }

    void decrease(){
        synchronized (this){
            for (int i = 0; i < 1000000; i++) {
                sold -= 1;
            }
        }
    }

    public int getSold() {
        return sold;
    }
}
