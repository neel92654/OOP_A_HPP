package util;

public final class BankResource implements AutoCloseable {

    private boolean closed;

    public BankResource() {
        System.out.println("MiniBank resource opened.");
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            System.out.println("MiniBank resource closed.");
        }
    }
}
