package model;

public interface InterestBearing {

    double interestRate();

    long getBalance();

    default double yearlyInterest() {
        return calculateInterest(getBalance(), interestRate());
    }

    private double calculateInterest(long balance, double rate) {
        return balance * rate / 100.0;
    }

    static double calculateInterestFor(long balance, double rate) {
        return balance * rate / 100.0;
    }
}
