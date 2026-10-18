package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import model.Customer;
import util.Repository;
import util.ReportGenerator;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;
import util.Validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BankService {
    private final Repository<Account> accounts;
    private final Repository<Customer> customers = new Repository<>();
    private final Path stateFile;
    private final Path logDirectory;
    private final Path reportFile;

    public BankService(Repository<Account> accounts, Path stateFile, Path logDirectory, Path reportFile) {
        this.accounts=accounts;this.stateFile=stateFile;this.logDirectory=logDirectory;this.reportFile=reportFile;
    }
    public Account openAccount(String type,String owner,String email,String mobile,long openingBalance) throws BankException {
        if(owner==null||owner.isBlank())throw new InvalidAmountException("Owner name is required");
        if(!Validator.isValidEmail(email))throw new InvalidAmountException("Invalid email address");
        if(!Validator.isValidMobile(mobile))throw new InvalidAmountException("Invalid mobile number");
        if(openingBalance<0)throw new InvalidAmountException("Opening balance cannot be negative");
        Account account=switch(type.toUpperCase()){
            case "SAVINGS" -> new SavingsAccount(owner,openingBalance,0);
            case "CURRENT" -> new CurrentAccount(owner,openingBalance,1000);
            case "FIXED" -> new FixedDepositAccount(owner,openingBalance);
            default -> throw new InvalidAmountException("Account type must be SAVINGS, CURRENT or FIXED");
        };
        accounts.save(account);customers.save(new Customer(owner,email,mobile));return account;
    }
    public void deposit(String id,long amount)throws BankException,IOException{Account a=require(id);a.deposit(amount);TransactionLog.append(logDirectory.resolve("transactions.log"),"DEPOSIT "+id+" "+amount);}
    public void withdraw(String id,long amount)throws BankException,IOException{Account a=require(id);a.withdraw(amount);TransactionLog.append(logDirectory.resolve("transactions.log"),"WITHDRAW "+id+" "+amount);}
    public void transfer(String fromId,String toId,long amount)throws BankException,IOException{Account from=require(fromId),to=require(toId);from.transfer(to,amount);TransactionLog.append(logDirectory.resolve("transactions.log"),"WITHDRAW "+fromId+" "+amount);TransactionLog.append(logDirectory.resolve("transactions.log"),"DEPOSIT "+toId+" "+amount);}
    public String statement(String id)throws AccountNotFoundException{return StatementFormatter.buildStatement(require(id));}
    public List<Account> listAccounts(){return accounts.findAll();}
    public Account getAccount(String id){return accounts.findById(id);}
    public String generateReport()throws IOException{return ReportGenerator.generate(logDirectory,reportFile);}
    public void save()throws IOException{StatePersister.save(accounts.findAll().toArray(Account[]::new),stateFile);}
    public void processConcurrentDeposits(String id,long amount,int count)throws InterruptedException,AccountNotFoundException,InvalidAmountException,IOException{
        if(count<1||count>10000)throw new InvalidAmountException("Batch count must be 1-10000");if(amount<=0)throw new InvalidAmountException("Amount must be positive");Account account=require(id);
        try(TransactionProcessor processor=new TransactionProcessor(accounts.findAll())){java.util.concurrent.CountDownLatch done=new java.util.concurrent.CountDownLatch(count);for(int i=0;i<count;i++)processor.submit(()->{try{Account shared=processor.get(id);if(shared!=null)shared.deposit(amount);}catch(InvalidAmountException e){System.err.println(e.getMessage());}finally{done.countDown();}});done.await();}
        for(int i=0;i<count;i++)TransactionLog.append(logDirectory.resolve("transactions.log"),"DEPOSIT "+id+" "+amount);
        System.out.println("Processed "+count+" deposits. Balance: "+account.getBalance());
    }
    private Account require(String id)throws AccountNotFoundException{Account a=accounts.findById(id);if(a==null)throw new AccountNotFoundException("Account not found: "+id);return a;}
}
