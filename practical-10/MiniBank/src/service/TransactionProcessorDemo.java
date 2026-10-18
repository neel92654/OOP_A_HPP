package service;
import model.Account;import model.SavingsAccount;
public class TransactionProcessorDemo {
    public static void main(String[] args){Account account=new SavingsAccount("Batch",0,0);TransactionProcessor processor=new TransactionProcessor();for(int i=0;i<10;i++)processor.submit(()->{for(int j=0;j<1000;j++)try{account.deposit(1);}catch(Exception e){throw new RuntimeException(e);}});processor.stop();System.out.println("Final balance after batch: "+account.getBalance()+" (expected 10000)");}
}
