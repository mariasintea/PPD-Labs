#include "Barrier.h"

Barrier::Barrier(int count) : thread_count(count), counter(0), waiting(0){}

void Barrier::wait()
{
    unique_lock<mutex> lk(m);
    ++counter;
    ++waiting;
    cv.wait(lk, [&]{return counter >= thread_count;});
    cv.notify_one();
    --waiting;
    if(waiting == 0)
    counter = 0;
    lk.unlock();
}