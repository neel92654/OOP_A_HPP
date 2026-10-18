package service;
import exception.InvalidAmountException;import model.Account;import java.util.Map;import java.util.concurrent.*;
public class TransactionProcessor implements AutoCloseable {
    private final ExecutorService executor=Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String,Account> sharedAccounts=new ConcurrentHashMap<>();
    public TransactionProcessor(Iterable<Account> accounts){for(Account a:accounts)register(a);}
    public void register(Account account){sharedAccounts.put(account.getAccountNumber(),account);}
    public Account get(String id){return sharedAccounts.get(id);}
    public void submit(Runnable task){executor.execute(task);}
    public void runDeposits(String id,long amount,int count){Account account=sharedAccounts.get(id);if(account==null){System.out.println("Account not found in shared map.");return;}CountDownLatch done=new CountDownLatch(count);for(int i=0;i<count;i++)submit(()->{try{account.deposit(amount);}catch(InvalidAmountException e){System.out.println(e.getMessage());}finally{done.countDown();}});try{done.await();}catch(InterruptedException e){Thread.currentThread().interrupt();} }
    public void stop(){executor.shutdown();try{if(!executor.awaitTermination(30,TimeUnit.SECONDS))executor.shutdownNow();}catch(InterruptedException e){executor.shutdownNow();Thread.currentThread().interrupt();}}
    @Override public void close(){stop();}
}
