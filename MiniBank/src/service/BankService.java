package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.Repository;
import util.ReportGenerator;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;
import util.WithdrawRule;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static util.Validator.*;

public class BankService {

    private final Repository<Account> accounts;
    private final Repository<Customer> customers = new Repository<>();
    private final Path stateFile;
    private final Path logDirectory;
    private final Path reportFile;

    public BankService(Repository<Account> accounts, Path stateFile,
                       Path logDirectory, Path reportFile) {
        this.accounts = accounts;
        this.stateFile = stateFile;
        this.logDirectory = logDirectory;
        this.reportFile = reportFile;
    }

    public Account openAccount(String type, String owner, String email,
                              String mobile, long openingBalance)
            throws BankException {
        if (owner == null || owner.isBlank()) {
            throw new InvalidAmountException("Owner name is required");
        }
        if (!isValidEmail(email)) {
            throw new InvalidAmountException("Invalid email address");
        }
        if (!isValidMobile(mobile)) {
            throw new InvalidAmountException("Invalid mobile number");
        }
        if (openingBalance < 0) {
            throw new InvalidAmountException(
                    "Opening balance cannot be negative");
        }
        if (type == null) {
            throw new InvalidAmountException("Account type is required");
        }

        Account account = switch (type.toUpperCase(Locale.ROOT)) {
            case "SAVINGS" -> new SavingsAccount(owner, openingBalance, 0);
            case "CURRENT" -> new CurrentAccount(owner, openingBalance, 1000);
            case "FIXED" -> new FixedDepositAccount(owner, openingBalance);
            default -> throw new InvalidAmountException(
                    "Account type must be SAVINGS, CURRENT or FIXED");
        };

        accounts.save(account);
        customers.save(new Customer(owner, email, mobile));
        return account;
    }

    public void deposit(String id, long amount)
            throws BankException, IOException {
        Account account = require(id);
        account.deposit(amount);
        TransactionLog.append(logDirectory.resolve("transactions.log"),
                "DEPOSIT " + id + " " + amount);
    }

    public void withdraw(String id, long amount)
            throws BankException, IOException {
        Account account = require(id);
        account.withdraw(amount);
        TransactionLog.append(logDirectory.resolve("transactions.log"),
                "WITHDRAW " + id + " " + amount);
    }

    public void transfer(String fromId, String toId, long amount)
            throws BankException, IOException {
        Account from = require(fromId);
        Account to = require(toId);
        from.transfer(to, amount);
        TransactionLog.append(logDirectory.resolve("transactions.log"),
                "WITHDRAW " + fromId + " " + amount);
        TransactionLog.append(logDirectory.resolve("transactions.log"),
                "DEPOSIT " + toId + " " + amount);
    }

    public String statement(String id) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(require(id));
    }

    public List<Account> listAccounts() {
        ArrayList<Account> result = accounts.findAll();
        result.sort(Comparator.comparingLong(Account::getBalance).reversed()
                .thenComparing(Account::getAccountNumber));
        return result;
    }

    public Account getAccount(String id) {
        return accounts.findById(id);
    }

    public String generateReport() throws IOException {
        return ReportGenerator.generate(logDirectory, reportFile);
    }

    public void save() throws IOException {
        StatePersister.save(accounts.findAll().toArray(Account[]::new), stateFile);
    }

    public void processConcurrentDeposits(String id, long amount, int count)
            throws InterruptedException, BankException, IOException {
        if (count < 1 || count > 10000) {
            throw new InvalidAmountException("Batch count must be 1-10000");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }

        Account account = require(id);
        AtomicReference<BankException> failure = new AtomicReference<>();
        List<Runnable> tasks = new ArrayList<>();

        try (TransactionProcessor processor =
                     new TransactionProcessor(accounts.findAll())) {
            for (int i = 0; i < count; i++) {
                tasks.add(() -> {
                    Account shared = processor.get(id);
                    try {
                        if (shared == null) {
                            throw new AccountNotFoundException(
                                    "Account not found: " + id);
                        }
                        shared.deposit(amount);
                    } catch (BankException e) {
                        failure.compareAndSet(null, e);
                    }
                });
            }
            processor.processBatch(tasks);
        }

        if (failure.get() != null) {
            throw failure.get();
        }

        Path transactionFile = logDirectory.resolve("transactions.log");
        for (int i = 0; i < count; i++) {
            TransactionLog.append(transactionFile,
                    "DEPOSIT " + id + " " + amount);
        }
        System.out.println("Processed " + count + " deposits. Balance: "
                + account.getBalance());
    }

    private Account require(String id) throws AccountNotFoundException {
        Account account = accounts.findById(id);
        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + id);
        }
        return account;
    }

    public boolean checkWithdrawRuleExamples(Account account, long amount) {
        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account target, long value) {
                return target.canWithdraw(value);
            }
        };
        WithdrawRule lambdaRule = (target, value) -> target.canWithdraw(value);
        return anonymousRule.allow(account, amount)
                == lambdaRule.allow(account, amount);
    }
}
