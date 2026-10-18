package service;

import model.Account;
import util.TransactionBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor implements AutoCloseable {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> sharedAccounts =
            new ConcurrentHashMap<>();

    public TransactionProcessor(Iterable<Account> accounts) {
        for (Account account : accounts) {
            sharedAccounts.put(account.getAccountNumber(), account);
        }
    }

    public void register(Account account) {
        sharedAccounts.put(account.getAccountNumber(), account);
    }

    public Account get(String accountNumber) {
        return sharedAccounts.get(accountNumber);
    }

    public void submit(Runnable task) {
        executor.execute(task);
    }

    public void processBatch(List<Runnable> tasks) throws InterruptedException {
        if (tasks.isEmpty()) {
            return;
        }

        TransactionBuffer buffer = new TransactionBuffer(8);
        CountDownLatch completed = new CountDownLatch(tasks.size());

        Thread dispatcher = new Thread(() -> {
            try {
                while (true) {
                    Runnable task = buffer.take();
                    if (task == null) {
                        break;
                    }
                    executor.execute(() -> {
                        try {
                            task.run();
                        } finally {
                            completed.countDown();
                        }
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "transaction-dispatcher");

        dispatcher.start();
        try {
            for (Runnable task : tasks) {
                buffer.put(task);
            }
            buffer.put(null);
            dispatcher.join();
            completed.await();
        } catch (InterruptedException e) {
            dispatcher.interrupt();
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    public void runDeposits(String accountNumber, long amount, int count)
            throws InterruptedException {
        Account account = sharedAccounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tasks.add(() -> {
                try {
                    account.deposit(amount);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            });
        }
        processBatch(tasks);
    }

    public void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        stop();
    }
}
