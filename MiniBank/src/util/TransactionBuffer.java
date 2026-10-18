package util;

public class TransactionBuffer {

    private final Runnable[] items;
    private int head;
    private int tail;
    private int size;

    public TransactionBuffer(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        items = new Runnable[capacity];
    }

    public synchronized void put(Runnable task) throws InterruptedException {
        while (size == items.length) {
            wait();
        }
        items[tail] = task;
        tail = (tail + 1) % items.length;
        size++;
        notifyAll();
    }

    public synchronized Runnable take() throws InterruptedException {
        while (size == 0) {
            wait();
        }
        Runnable task = items[head];
        items[head] = null;
        head = (head + 1) % items.length;
        size--;
        notifyAll();
        return task;
    }
}
