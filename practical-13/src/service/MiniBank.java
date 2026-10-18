package service;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.SavingsAccount;
import util.Repository;
import util.StatementFormatter;
import util.Validator;
import util.ReportGenerator;
import util.StatePersister;
import util.TransactionLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MiniBank {
    private static final Path DATA = Path.of("data");
    private static final Path STATE = DATA.resolve("accounts.dat");
    private static final Path LOGS = DATA.resolve("logs");
    private static final Path REPORT = DATA.resolve("report.txt");
    private static final Repository<Account> accounts = new Repository<>();

    public static void main(String[] args) {
        try {
            Files.createDirectories(LOGS);
            if (Files.exists(STATE)) {
                for (Account account : StatePersister.load(STATE)) accounts.save(account);
                System.out.println("Loaded " + accounts.findAll().size() + " account(s).");
            }
            runMenu();
        } catch (Exception e) {
            System.out.println("MiniBank could not start: " + e.getMessage());
        } finally {
            try { StatePersister.save(accounts.findAll().toArray(Account[]::new), STATE); }
            catch (Exception e) { System.out.println("Could not save accounts: " + e.getMessage()); }
        }
    }

    private static void runMenu() throws Exception {
        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\nMINIBANK - Practical 13");
                System.out.println("1. Open account  2. Deposit  3. Withdraw  4. Transfer");
                System.out.println("5. List accounts  6. Statement  7. Report  8. Repository demo  9. Exit");
                System.out.print("Choose: ");
                String input = sc.nextLine().trim();
                int choice;
                try { choice = Integer.parseInt(input); } catch (NumberFormatException e) { System.out.println("Enter a menu number."); continue; }
                switch (choice) {
                    case 1 -> open(sc);
                    case 2 -> deposit(sc);
                    case 3 -> withdraw(sc);
                    case 4 -> transfer(sc);
                    case 5 -> accounts.findAll().forEach(System.out::println);
                    case 6 -> statement(sc);
                    case 7 -> report();
                    case 8 -> repositoryDemo();
                    case 9 -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            }
        }
        StatePersister.save(accounts.findAll().toArray(Account[]::new), STATE);
        System.out.println("Saved account data. Goodbye.");
    }
    private static void open(Scanner sc) throws Exception {
        System.out.print("Owner name: "); String owner = sc.nextLine().trim();
        if (owner.isBlank()) { System.out.println("Owner name is required."); return; }
        System.out.print("Opening balance (0 or more): "); long balance = readLong(sc);
        if (balance < 0) { System.out.println("Opening balance cannot be negative."); return; }
        Account a = new SavingsAccount(owner, balance, 0);
        accounts.save(a); System.out.println("Account opened: " + a.getAccountNumber());
    }
    private static void deposit(Scanner sc) throws Exception {
        Account a = findPrompt(sc); if (a == null) return;
        System.out.print("Amount: "); long amount = readLong(sc);
        a.deposit(amount); log("DEPOSIT " + a.getAccountNumber() + " " + amount);
        System.out.println("New balance: " + a.getBalance());
    }
    private static void withdraw(Scanner sc) throws Exception {
        Account a = findPrompt(sc); if (a == null) return;
        System.out.print("Amount: "); long amount = readLong(sc);
        a.withdraw(amount); log("WITHDRAW " + a.getAccountNumber() + " " + amount);
        System.out.println("New balance: " + a.getBalance());
    }
    private static void transfer(Scanner sc) throws Exception {
        System.out.print("From account: "); Account from = accounts.findById(sc.nextLine().trim());
        System.out.print("To account: "); Account to = accounts.findById(sc.nextLine().trim());
        if (from == null || to == null) { System.out.println("Account not found."); return; }
        System.out.print("Amount: "); long amount = readLong(sc); from.transfer(to, amount);
        log("WITHDRAW " + from.getAccountNumber() + " " + amount);
        log("DEPOSIT " + to.getAccountNumber() + " " + amount);
        System.out.println("Transfer successful.");
    }
    private static void statement(Scanner sc) { Account a = findPrompt(sc); if (a != null) System.out.println(StatementFormatter.buildStatement(a)); }
    private static void report() throws IOException { ReportGenerator.generate(LOGS, REPORT); System.out.println(Files.readString(REPORT)); }
    private static void repositoryDemo() {
        System.out.println("Repository uses @Id to save any entity type.");
        for (Account a : accounts.findAll()) System.out.println(a.getAccountNumber() + " -> " + accounts.findById(a.getAccountNumber()));
    }
    private static Account findPrompt(Scanner sc) { System.out.print("Account number: "); Account a = accounts.findById(sc.nextLine().trim()); if (a == null) System.out.println("Account not found."); return a; }
    private static long readLong(Scanner sc) { try { return Long.parseLong(sc.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Invalid number."); return -1; } }
    private static void log(String line) throws IOException { TransactionLog.append(LOGS.resolve("transactions.log"), line); }
}
