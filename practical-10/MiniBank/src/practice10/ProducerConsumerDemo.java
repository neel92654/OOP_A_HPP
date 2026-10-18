package practice10;
public class ProducerConsumerDemo {
    static class Buffer {
        private final int[] values=new int[3];private int size,head,tail;
        synchronized void put(int value)throws InterruptedException{while(size==values.length)wait();values[tail]=value;tail=(tail+1)%values.length;size++;System.out.println("Produced "+value);notifyAll();}
        synchronized int take()throws InterruptedException{while(size==0)wait();int value=values[head];head=(head+1)%values.length;size--;System.out.println("Consumed "+value);notifyAll();return value;}
    }
    public static void main(String[] args)throws InterruptedException{Buffer b=new Buffer();Thread producer=new Thread(()->{try{for(int i=1;i<=10;i++)b.put(i);}catch(InterruptedException e){Thread.currentThread().interrupt();}});Thread consumer=new Thread(()->{try{for(int i=1;i<=10;i++)b.take();}catch(InterruptedException e){Thread.currentThread().interrupt();}});producer.start();consumer.start();producer.join();consumer.join();System.out.println("All items produced and consumed in order.");}
}
