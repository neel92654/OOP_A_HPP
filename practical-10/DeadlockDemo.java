package practice10;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
public class DeadlockDemo {
    private static void demonstrateDeadlock() throws InterruptedException {
        Object a=new Object(), b=new Object();CountDownLatch firstLocks=new CountDownLatch(2);
        Thread t1=new Thread(()->{synchronized(a){firstLocks.countDown();await(firstLocks);synchronized(b){}}},"A-then-B");
        Thread t2=new Thread(()->{synchronized(b){firstLocks.countDown();await(firstLocks);synchronized(a){}}},"B-then-A");
        t1.setDaemon(true);t2.setDaemon(true);t1.start();t2.start();t1.join(300);t2.join(300);
        System.out.println("Opposite lock order causes deadlock: "+(t1.isAlive()&&t2.isAlive()));
    }
    private static void await(CountDownLatch latch){try{latch.await();}catch(InterruptedException e){Thread.currentThread().interrupt();}}
    private static void safeRun() throws InterruptedException {
        ReentrantLock low=new ReentrantLock(), high=new ReentrantLock();
        Runnable transfer=()->{for(int i=0;i<100;i++){low.lock();try{high.lock();try{}finally{high.unlock();}}finally{low.unlock();}}};
        Thread t1=new Thread(transfer),t2=new Thread(transfer);t1.start();t2.start();t1.join();t2.join();System.out.println("Consistent lock order completes: true");
    }
    public static void main(String[] args)throws Exception{demonstrateDeadlock();safeRun();}
}
