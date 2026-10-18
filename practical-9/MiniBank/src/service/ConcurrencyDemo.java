package service;
import model.Account;
import model.SavingsAccount;
public class ConcurrencyDemo {
    public static void main(String[] args) throws InterruptedException {
        Account account=new SavingsAccount("Concurrent Test",0,0);Thread[] threads=new Thread[10];
        for(int i=0;i<threads.length;i++){threads[i]=new Thread(new AccountWorker(account,1000,1),"worker-"+(i+1));threads[i].start();}
        for(Thread t:threads)t.join();System.out.println("Final synchronized MiniBank balance: "+account.getBalance()+" (expected 10000)");
    }
}
