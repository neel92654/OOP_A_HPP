package practice10;
import java.util.concurrent.*;
public class ThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool=Executors.newFixedThreadPool(3);
        for(int i=1;i<=10;i++){final int id=i;pool.submit(()->{System.out.println("Task "+id+" on "+Thread.currentThread().getName());try{Thread.sleep(100);}catch(InterruptedException e){Thread.currentThread().interrupt();}});}
        pool.shutdown();if(!pool.awaitTermination(10,TimeUnit.SECONDS))pool.shutdownNow();
        System.out.println("All tasks finished.");
    }
}
