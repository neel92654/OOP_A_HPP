package service;

import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import util.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class MiniBank {
    private static final Path DATA=Path.of("data");
    private static final Path STATE=DATA.resolve("accounts.dat");
    private static final Path LOGS=DATA.resolve("logs");
    private static final Path REPORT=DATA.resolve("report.txt");
    private static final Repository<Account> repository=new Repository<>();
    private static BankService bank;

    public static void main(String[] args){
        try{
            Files.createDirectories(LOGS);
            if(Files.exists(STATE))for(Account a:util.StatePersister.load(STATE))repository.save(a);
            bank=new BankService(repository,STATE,LOGS,REPORT);
            runMenu();
        }catch(Exception e){System.out.println("MiniBank error: "+e.getMessage());}
        finally{try{if(bank!=null)bank.save();}catch(Exception e){System.out.println("Could not save: "+e.getMessage());}}
    }
    private static void runMenu()throws Exception{
        try(Scanner sc=new Scanner(System.in)){boolean running=true;while(running){
            System.out.println("\nMINIBANK - Practical 14");
            System.out.println("1 Open account  2 Deposit  3 Withdraw  4 Transfer");
            System.out.println("5 Statement  6 List accounts  7 Report  8 Batch deposits  9 Exit");
            System.out.print("Choose: ");int choice;try{choice=Integer.parseInt(sc.nextLine().trim());}catch(NumberFormatException e){System.out.println("Enter a menu number.");continue;}
            try{switch(choice){case 1->open(sc);case 2->deposit(sc);case 3->withdraw(sc);case 4->transfer(sc);case 5->statement(sc);case 6->bank.listAccounts().forEach(System.out::println);case 7->System.out.println(bank.generateReport());case 8->batch(sc);case 9->{bank.save();running=false;}default->System.out.println("Invalid option.");}}catch(BankException|java.io.IOException e){System.out.println("Operation failed: "+e.getMessage());}catch(InterruptedException e){Thread.currentThread().interrupt();System.out.println("Operation interrupted.");}
        }}System.out.println("Account state saved. Goodbye.");
    }
    private static void open(Scanner sc)throws BankException{
        System.out.print("Owner name: ");String owner=sc.nextLine().trim();System.out.print("Email: ");String email=sc.nextLine().trim();System.out.print("Mobile (10 digits starting 6-9): ");String mobile=sc.nextLine().trim();
        System.out.print("Type (SAVINGS/CURRENT/FIXED): ");String type=sc.nextLine().trim();System.out.print("Opening balance: ");long balance=readLong(sc);
        Account a=bank.openAccount(type,owner,email,mobile,balance);System.out.println("Account opened: "+a);
    }
    private static void deposit(Scanner sc)throws Exception{String id=id(sc);long amount=amount(sc);bank.deposit(id,amount);System.out.println("Deposit successful. Balance: "+bank.getAccount(id).getBalance());}
    private static void withdraw(Scanner sc)throws Exception{String id=id(sc);long amount=amount(sc);bank.withdraw(id,amount);System.out.println("Withdrawal successful. Balance: "+bank.getAccount(id).getBalance());}
    private static void transfer(Scanner sc)throws Exception{System.out.print("From account: ");String from=sc.nextLine().trim();System.out.print("To account: ");String to=sc.nextLine().trim();long amount=amount(sc);bank.transfer(from,to,amount);System.out.println("Transfer successful.");}
    private static void statement(Scanner sc)throws Exception{System.out.println(bank.statement(id(sc)));}
    private static void batch(Scanner sc)throws Exception{String id=id(sc);long amount=amount(sc);System.out.print("Number of deposits (1-10000): ");int count=(int)readLong(sc);bank.processConcurrentDeposits(id,amount,count);}
    private static String id(Scanner sc){System.out.print("Account number: ");return sc.nextLine().trim();}
    private static long amount(Scanner sc){System.out.print("Amount: ");return readLong(sc);}
    private static long readLong(Scanner sc){try{return Long.parseLong(sc.nextLine().trim());}catch(NumberFormatException e){System.out.println("Invalid number.");return -1;}}
}
