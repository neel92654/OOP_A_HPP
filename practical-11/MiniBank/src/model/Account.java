package model;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

import java.util.Objects;
import java.io.Serializable;

public abstract class Account implements Transactable, InterestBearing, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @MaxLength(20)
    private final String accountNumber;
    private String ownerName;
    @Positive
    private long balance;
    private boolean active;
    private static long accountCounter = 0;

    public static synchronized void observeAccountNumber(String number) {
        if (number != null && number.matches("AC\\d+")) {
            try {
                accountCounter = Math.max(accountCounter, Long.parseLong(number.substring(2)));
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    public Account(String ownerName, long balance) {
        this(generateAccountNumber(), ownerName, balance);
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    protected Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        observeAccountNumber(accountNumber);
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public void closeAccount() {
        active = false;
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0");
        }
        if (!active) {
            throw new InvalidAmountException("Account is inactive");
        }
        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0");
        }
        if (!active) {
            throw new InvalidAmountException("Account is inactive");
        }
        if (!canWithdraw(amount)) {
            long available = getAvailableForWithdrawal();
            long shortfall = amount - available;
            throw new InsufficientFundsException(
                    "Insufficient funds. Shortfall: " + shortfall, shortfall);
        }
        balance -= amount;
        return true;
    }

    protected long getAvailableForWithdrawal() {
        return balance;
    }

    public void transfer(Account to, long amount) throws BankException {
        if (to == null) {
            throw new AccountNotFoundException("Destination account not found");
        }
        if (to == this) {
            if (amount <= 0) throw new InvalidAmountException("Transfer amount must be greater than 0");
            return;
        }
        Account first = accountNumber.compareTo(to.accountNumber) < 0 ? this : to;
        Account second = first == this ? to : this;
        synchronized (first) {
            synchronized (second) {
                if (amount <= 0) throw new InvalidAmountException("Transfer amount must be greater than 0");
                if (!active || !to.active) throw new InvalidAmountException("Both accounts must be active");
                if (!canWithdraw(amount)) {
                    long shortfall = amount - getAvailableForWithdrawal();
                    throw new InsufficientFundsException("Insufficient funds. Shortfall: " + shortfall, shortfall);
                }
                balance -= amount;
                to.balance += amount;
            }
        }
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account)) {
            return false;
        }
        Account account = (Account) o;
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);
}
