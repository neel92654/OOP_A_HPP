package service;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.SavingsAccount;
import util.StatementFormatter;

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
    private static final List<Account> accounts = new ArrayList<>();

    public static void main(String[] args) {
        try {
            Files.createDirectories(LOGS);
            runMenu();
        } catch (Exception e) { System.out.println("MiniBank error: " + e.getMessage()); }
        finally {

        }
    }
    private static void runMenu() throws Exception {
        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\nMINIBANK - Practical 10");
                System.out.println("1 Open account  2 Deposit  3 Withdraw  4 Transfer  5 List accounts" + "  6 Exit");
                System.out.print("Choose: ");
                int choice; try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Enter a menu number."); continue; }
                switch (choice) {
                    case 1 -> open(sc);
                    case 2 -> deposit(sc);
                    case 3 -> withdraw(sc);
                    case 4 -> transfer(sc);
                    case 5 -> { for (Account a : accounts) System.out.println(a); }
                    case 6 -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            }
        }

        System.out.println("Goodbye.");
    }
    private static void open(Scanner sc) throws Exception {
        System.out.print("Owner name: "); String owner = sc.nextLine().trim();
        if (owner.isBlank()) { System.out.println("Owner name is required."); return; }
        System.out.print("Opening balance: "); long balance = readLong(sc);
        if (balance < 0) { System.out.println("Balance cannot be negative."); return; }
        Account a = new SavingsAccount(owner, balance, 0); accounts.add(a); System.out.println("Opened: " + a);
    }
    private static void deposit(Scanner sc) throws Exception {
        Account a = find(sc); if (a == null) return;
        System.out.print("Amount: "); long amount = readLong(sc); a.deposit(amount);

        System.out.println("Balance: " + a.getBalance());
    }
    private static void withdraw(Scanner sc) throws Exception {
        Account a = find(sc); if (a == null) return;
        System.out.print("Amount: "); long amount = readLong(sc); a.withdraw(amount);

        System.out.println("Balance: " + a.getBalance());
    }
    private static void transfer(Scanner sc) throws Exception {
        System.out.print("From account: "); Account from = findById(sc.nextLine().trim());
        System.out.print("To account: "); Account to = findById(sc.nextLine().trim());
        if (from == null || to == null) { System.out.println("Account not found."); return; }
        System.out.print("Amount: "); long amount = readLong(sc); from.transfer(to, amount);

        System.out.println("Transfer successful.");
    }
    private static Account find(Scanner sc) { System.out.print("Account number: "); Account a = findById(sc.nextLine().trim()); if (a == null) System.out.println("Account not found."); return a; }
    private static Account findById(String id) { for (Account a : accounts) if (a.getAccountNumber().equals(id)) return a; return null; }
    private static long readLong(Scanner sc) { try { return Long.parseLong(sc.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Invalid number."); return -1; } }

}
