#ifndef LAB2_C___BARRIER_H
#define LAB2_C___BARRIER_H

#include <mutex>
#include <condition_variable>

using namespace std;

class Barrier {
private:
    mutex m;
    condition_variable cv;
    int counter;
    int waiting;
    int thread_count;
public:
    Barrier(int);

    void wait();
};


#endif
