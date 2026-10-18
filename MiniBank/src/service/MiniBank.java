package service;

import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.BankInfo;
import model.MenuOption;
import util.BankResource;
import util.Repository;
import util.StatePersister;
import util.WithdrawRule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class MiniBank {

    private static final Path DATA = Path.of("data");
    private static final Path STATE = DATA.resolve("accounts.dat");
    private static final Path LOGS = DATA.resolve("logs");
    private static final Path REPORT = DATA.resolve("report.txt");

    private static final Repository<Account> repository = new Repository<>();
    private static BankService bank;

    @SuppressWarnings("try")
    public static void main(String[] args) {
        try (BankResource resource = new BankResource()) {
            System.out.println(new BankInfo("MiniBank", "Main Branch"));
            Files.createDirectories(LOGS);
            if (Files.exists(STATE)) {
                for (Account account : StatePersister.load(STATE)) {
                    repository.save(account);
                }
            }
            bank = new BankService(repository, STATE, LOGS, REPORT);
            runMenu();
        } catch (Exception e) {
            System.out.println("MiniBank error: " + e.getMessage());
        } finally {
            try {
                if (bank != null) {
                    bank.save();
                }
            } catch (IOException e) {
                System.out.println("Could not save account state: " + e.getMessage());
            }
        }
    }

    private static void runMenu() throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                System.out.print("Choose: ");
                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Enter a menu number.");
                    continue;
                }

                MenuOption option = switch (choice) {
                    case 1 -> MenuOption.OPEN_ACCOUNT;
                    case 2 -> MenuOption.DEPOSIT;
                    case 3 -> MenuOption.WITHDRAW;
                    case 4 -> MenuOption.TRANSFER;
                    case 5 -> MenuOption.STATEMENT;
                    case 6 -> MenuOption.LIST_ACCOUNTS;
                    case 7 -> MenuOption.REPORT;
                    case 8 -> MenuOption.BATCH_DEPOSITS;
                    case 9 -> MenuOption.EXIT;
                    default -> null;
                };

                if (option == null) {
                    System.out.println("Invalid option.");
                    continue;
                }

                try {
                    switch (option) {
                        case OPEN_ACCOUNT -> openAccount(scanner);
                        case DEPOSIT -> deposit(scanner);
                        case WITHDRAW -> withdraw(scanner);
                        case TRANSFER -> transfer(scanner);
                        case STATEMENT -> System.out.println(
                                bank.statement(readAccountNumber(scanner)));
                        case LIST_ACCOUNTS -> listAccounts();
                        case REPORT -> System.out.println(bank.generateReport());
                        case BATCH_DEPOSITS -> batchDeposits(scanner);
                        case EXIT -> {
                            bank.save();
                            running = false;
                        }
                    }
                } catch (BankException | IOException e) {
                    System.out.println("Operation failed: " + e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Operation interrupted.");
                }
            }
        }
        System.out.println("Account state saved. Goodbye.");
    }

    private static void printMenu() {
        System.out.println("\nMINIBANK - FINAL IMPLEMENTATION");
        System.out.println("1 Open account  2 Deposit  3 Withdraw  4 Transfer");
        System.out.println("5 Statement  6 List accounts  7 Report  8 Batch deposits  9 Exit");
    }

    private static void openAccount(Scanner scanner) throws BankException {
        System.out.print("Owner name: ");
        String owner = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Mobile (10 digits starting 6-9): ");
        String mobile = scanner.nextLine().trim();
        System.out.print("Type (SAVINGS/CURRENT/FIXED): ");
        String type = scanner.nextLine().trim();
        System.out.print("Opening balance: ");
        long balance = readLong(scanner);

        Account account = bank.openAccount(type, owner, email, mobile, balance);
        System.out.println("Account opened: " + account);
    }

    private static void deposit(Scanner scanner)
            throws BankException, IOException {
        String id = readAccountNumber(scanner);
        long amount = readAmount(scanner);
        bank.deposit(id, amount);
        System.out.println("Deposit successful. Balance: "
                + bank.getAccount(id).getBalance());
    }

    private static void withdraw(Scanner scanner)
            throws BankException, IOException {
        String id = readAccountNumber(scanner);
        long amount = readAmount(scanner);
        Account account = bank.getAccount(id);
        if (account != null) {
            if (!bank.checkWithdrawRuleExamples(account, amount)) {
                throw new InvalidAmountException("Withdrawal rules disagree");
            }
        }
        bank.withdraw(id, amount);
        System.out.println("Withdrawal successful. Balance: "
                + bank.getAccount(id).getBalance());
    }

    private static void transfer(Scanner scanner)
            throws BankException, IOException {
        System.out.print("From account: ");
        String from = scanner.nextLine().trim();
        System.out.print("To account: ");
        String to = scanner.nextLine().trim();
        long amount = readAmount(scanner);
        bank.transfer(from, to, amount);
        System.out.println("Transfer successful.");
    }

    private static void listAccounts() {
        var accounts = bank.listAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        accounts.forEach(System.out::println);
    }

    private static void batchDeposits(Scanner scanner)
            throws BankException, IOException, InterruptedException {
        String id = readAccountNumber(scanner);
        long amount = readAmount(scanner);
        System.out.print("Number of deposits (1-10000): ");
        int count = Math.toIntExact(readLong(scanner));
        bank.processConcurrentDeposits(id, amount, count);
    }

    private static String readAccountNumber(Scanner scanner) {
        System.out.print("Account number: ");
        return scanner.nextLine().trim();
    }

    private static long readAmount(Scanner scanner) {
        System.out.print("Amount: ");
        return readLong(scanner);
    }

    private static long readLong(Scanner scanner) {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Enter a whole number: ");
            }
        }
    }
}
