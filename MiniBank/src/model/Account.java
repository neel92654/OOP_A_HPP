package model;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

import java.io.Serializable;
import java.util.Objects;

public abstract class Account implements Transactable, InterestBearing,
        Comparable<Account>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @MaxLength(20)
    private final String accountNumber;

    private String ownerName;

    @Positive
    private long balance;

    private boolean active;
    private static long accountCounter;

    public static synchronized void observeAccountNumber(String number) {
        if (number != null && number.matches("AC\\d+")) {
            try {
                accountCounter = Math.max(accountCounter,
                        Long.parseLong(number.substring(2)));
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private static synchronized String generateAccountNumber() {
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

    public synchronized long getBalance() {
        return balance;
    }

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void closeAccount() {
        active = false;
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be greater than 0");
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
            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than 0");
        }
        if (!active) {
            throw new InvalidAmountException("Account is inactive");
        }
        if (!canWithdraw(amount)) {
            long shortfall = amount - getAvailableForWithdrawal();
            throw new InsufficientFundsException(
                    "Insufficient funds. Shortfall: " + shortfall,
                    shortfall);
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
            throw new InvalidAmountException(
                    "Source and destination accounts must be different");
        }

        Account first = accountNumber.compareTo(to.accountNumber) < 0
                ? this : to;
        Account second = first == this ? to : this;

        synchronized (first) {
            synchronized (second) {
                long originalFromBalance = balance;
                long originalToBalance = to.balance;
                boolean completed = false;

                try {
                    if (amount <= 0) {
                        throw new InvalidAmountException(
                                "Transfer amount must be greater than 0");
                    }
                    if (!active || !to.active) {
                        throw new InvalidAmountException(
                                "Both accounts must be active");
                    }
                    if (!canWithdraw(amount)) {
                        long shortfall = amount - getAvailableForWithdrawal();
                        throw new InsufficientFundsException(
                                "Insufficient funds. Shortfall: " + shortfall,
                                shortfall);
                    }

                    balance -= amount;
                    to.balance += amount;
                    completed = true;
                } catch (BankException e) {
                    throw e;
                } finally {
                    if (!completed) {
                        balance = originalFromBalance;
                        to.balance = originalToBalance;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return getAccountNumber() + " | " + getOwnerName() + " | " + getBalance();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Account other)) {
            return false;
        }
        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);
}
