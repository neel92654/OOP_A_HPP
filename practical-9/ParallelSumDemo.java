package practice09;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class ParallelSumDemo {

    static final int SIZE = 100_000;
    static final int THREADS = 8;

    static long unsafeSum() throws InterruptedException {
        long[] total = {0};
        CountDownLatch start = new CountDownLatch(1);
        Thread[] ts = new Thread[THREADS];

        for (int t = 0; t < THREADS; t++) {
            final int lo = t * SIZE / THREADS;
            final int hi = (t + 1) * SIZE / THREADS;
            ts[t] = new Thread(() -> {
                try {
                    start.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                for (int n = lo; n < hi; n++) {
                    long old = total[0];
                    if ((n & 127) == 0) {
                        Thread.yield();
                    }
                    total[0] = old + n;
                }
            });
            ts[t].start();
        }
        
        start.countDown();
        for (Thread t : ts) {
            t.join();
        }
        return total[0];
    }

    static long synchronizedSum() throws InterruptedException {
        Object lock = new Object();
        long[] total = {0};
        Thread[] ts = new Thread[THREADS];

        for (int t = 0; t < THREADS; t++) {
            final int lo = t * SIZE / THREADS;
            final int hi = (t + 1) * SIZE / THREADS;
            ts[t] = new Thread(() -> {
                long local = 0;
                for (int n = lo; n < hi; n++) {
                    local += n;
                }
                synchronized (lock) {
                    total[0] += local;
                }
            });
            ts[t].start();
        }
        
        for (Thread t : ts) {
            t.join();
        }
        return total[0];
    }

    static long atomicSum() throws InterruptedException {
        AtomicLong total = new AtomicLong();
        Thread[] ts = new Thread[THREADS];

        for (int t = 0; t < THREADS; t++) {
            final int lo = t * SIZE / THREADS;
            final int hi = (t + 1) * SIZE / THREADS;
            ts[t] = new Thread(() -> {
                long local = 0;
                for (int n = lo; n < hi; n++) {
                    local += n;
                }
                total.addAndGet(local);
            });
            ts[t].start();
        }
        
        for (Thread t : ts) {
            t.join();
        }
        return total.get();
    }

    public static void main(String[] args) throws Exception {
        long expected = (long) SIZE * (SIZE - 1) / 2;
        System.out.println("Expected: " + expected);
        System.out.println("Shared unsynchronized total: " + unsafeSum());
        System.out.println("Synchronized local partials: " + synchronizedSum());
        System.out.println("AtomicLong partials:         " + atomicSum());
    }
}
