package service;

import exception.BankException;
import model.Account;
import model.SavingsAccount;
import util.AccountStore;
import util.StatementFormatter;
import util.StatePersister;
import util.TransactionLog;
import util.ReportGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class MiniBank {
    private static final Path DATA = Path.of("data");
    private static final Path STATE = DATA.resolve("accounts.dat");
    private static final Path LOGS = DATA.resolve("logs");
    private static final Path REPORT = DATA.resolve("report.txt");
    private static final AccountStore store = new AccountStore();
    private static TransactionProcessor processor;

    public static void main(String[] args) {
        try {
            Files.createDirectories(LOGS);
            if (Files.exists(STATE)) for (Account a : StatePersister.load(STATE)) store.add(a);
            processor = new TransactionProcessor(store.getAll());
            runMenu();
        } catch (Exception e) { System.out.println("MiniBank error: " + e.getMessage()); }
        finally {
            if (processor != null) processor.stop();
            try { StatePersister.save(store.getAll().toArray(Account[]::new), STATE); }
            catch (Exception e) { System.out.println("Could not save accounts: " + e.getMessage()); }
        }
    }
    private static void runMenu() throws Exception {
        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\nMINIBANK - Practical 12");
                System.out.println("1 Open  2 Deposit  3 Withdraw  4 Transfer  5 List/sort  6 Statement  7 Report  8 Batch deposits  9 Exit");
                System.out.print("Choose: "); int n; try { n = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Enter a number."); continue; }
                switch (n) {
                    case 1 -> open(sc);
                    case 2 -> deposit(sc);
                    case 3 -> withdraw(sc);
                    case 4 -> transfer(sc);
                    case 5 -> { for (Account a : store.sortedByBalance()) System.out.println(a); System.out.println("Transactions: " + store.getHistory().size()); }
                    case 6 -> statement(sc);
                    case 7 -> report();
                    case 8 -> batch(sc);
                    case 9 -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            }
        }
        StatePersister.save(store.getAll().toArray(Account[]::new), STATE);
        System.out.println("Saved account data. Goodbye.");
    }
    private static void open(Scanner sc) throws Exception { System.out.print("Owner: "); String owner = sc.nextLine().trim(); if (owner.isBlank()) { System.out.println("Owner required."); return; } System.out.print("Opening balance: "); long balance = readLong(sc); if (balance < 0) { System.out.println("Invalid balance."); return; } Account a = new SavingsAccount(owner, balance, 0); store.add(a); processor.register(a); store.record("OPEN " + a.getAccountNumber()); System.out.println("Opened: " + a); }
    private static void deposit(Scanner sc) throws Exception { Account a=find(sc); if(a==null)return; System.out.print("Amount: "); long amount=readLong(sc); a.deposit(amount); log("DEPOSIT " + a.getAccountNumber() + " " + amount); store.record("DEPOSIT " + a.getAccountNumber() + " " + amount); System.out.println("Balance: " + a.getBalance()); }
    private static void withdraw(Scanner sc) throws Exception { Account a=find(sc); if(a==null)return; System.out.print("Amount: "); long amount=readLong(sc); a.withdraw(amount); log("WITHDRAW " + a.getAccountNumber() + " " + amount); store.record("WITHDRAW " + a.getAccountNumber() + " " + amount); System.out.println("Balance: " + a.getBalance()); }
    private static void transfer(Scanner sc) throws Exception { System.out.print("From: "); Account a=store.get(sc.nextLine().trim()); System.out.print("To: "); Account b=store.get(sc.nextLine().trim()); if(a==null||b==null){System.out.println("Account not found.");return;} System.out.print("Amount: "); long amount=readLong(sc); a.transfer(b,amount); log("WITHDRAW " + a.getAccountNumber() + " " + amount); log("DEPOSIT " + b.getAccountNumber() + " " + amount); store.record("TRANSFER " + a.getAccountNumber() + " -> " + b.getAccountNumber() + " " + amount); System.out.println("Transfer successful."); }
    private static void batch(Scanner sc) { System.out.print("Account number: "); String id=sc.nextLine().trim(); System.out.print("Deposit amount per task: "); long amount=readLong(sc); System.out.print("Number of deposits: "); int count=(int)readLong(sc); if(count<1||count>10000||amount<=0){System.out.println("Use positive amount and 1-10000 tasks.");return;} processor.runDeposits(id, amount, count); System.out.println("Batch complete."); }
    private static void statement(Scanner sc) { Account a=find(sc); if(a!=null)System.out.println(StatementFormatter.buildStatement(a)); }
    private static void report() throws IOException { ReportGenerator.generate(LOGS,REPORT); System.out.println(Files.readString(REPORT)); }
    private static Account find(Scanner sc) { System.out.print("Account number: "); Account a=store.get(sc.nextLine().trim()); if(a==null)System.out.println("Account not found."); return a; }
    private static long readLong(Scanner sc) { try{return Long.parseLong(sc.nextLine().trim());}catch(NumberFormatException e){System.out.println("Invalid number.");return -1;} }
    private static void log(String line) throws IOException { TransactionLog.append(LOGS.resolve("transactions.log"),line); }
}
