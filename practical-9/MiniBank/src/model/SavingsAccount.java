package model;

public class SavingsAccount extends Account {

    private long minBalance;

    public SavingsAccount(String ownerName, long balance, long minBalance) {
        super(ownerName, balance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return getBalance() - amount >= minBalance;
    }

    @Override
    protected long getAvailableForWithdrawal() {
        return Math.max(0, getBalance() - minBalance);
    }
}
