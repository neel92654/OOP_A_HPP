package practice09;
import java.util.concurrent.CountDownLatch;
public class CounterRaceDemo {
    static class Counter {
        int value;
        void unsafeIncrement(int i) { int current=value; if ((i & 255)==0) Thread.yield(); value=current+1; }
        synchronized void safeIncrement() { value++; }
    }
    private static int run(boolean safe) throws InterruptedException {
        int threads=8, iterations=100_000; Counter c=new Counter(); CountDownLatch start=new CountDownLatch(1); Thread[] workers=new Thread[threads];
        for(int t=0;t<threads;t++){ final int id=t; workers[t]=new Thread(()->{try{start.await();}catch(InterruptedException e){Thread.currentThread().interrupt();return;} for(int i=0;i<iterations;i++){if(safe)c.safeIncrement();else c.unsafeIncrement(i);}},"counter-"+id); workers[t].start(); }
        start.countDown(); for(Thread w:workers)w.join(); return c.value;
    }
    public static void main(String[] args) throws Exception { int expected=8*100_000; System.out.println("Expected: "+expected); System.out.println("Without synchronized: "+run(false)); System.out.println("With synchronized:    "+run(true)); }
}
