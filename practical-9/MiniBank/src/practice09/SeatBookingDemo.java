package practice09;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
public class SeatBookingDemo {
    static class UnsafeBooking {
        volatile int seatsLeft=5;
        AtomicInteger successful=new AtomicInteger();
        final CountDownLatch checked=new CountDownLatch(10);
        void book() { if(seatsLeft>0){ checked.countDown(); try{checked.await();}catch(InterruptedException e){Thread.currentThread().interrupt();return;} seatsLeft--; successful.incrementAndGet(); } }
    }
    static class SafeBooking {
        int seatsLeft=5; int successful;
        synchronized void book(){if(seatsLeft>0){seatsLeft--;successful++;}}
    }
    public static void main(String[] args) throws Exception {
        UnsafeBooking u=new UnsafeBooking(); Thread[] a=new Thread[10];
        for(int i=0;i<a.length;i++){a[i]=new Thread(u::book);a[i].start();} for(Thread t:a)t.join();
        SafeBooking s=new SafeBooking(); Thread[] b=new Thread[10];
        for(int i=0;i<b.length;i++){b[i]=new Thread(s::book);b[i].start();} for(Thread t:b)t.join();
        System.out.println("Unsafe successful bookings: "+u.successful.get()+", seats left: "+u.seatsLeft);
        System.out.println("Synchronized successful bookings: "+s.successful+", seats left: "+s.seatsLeft);
    }
}
