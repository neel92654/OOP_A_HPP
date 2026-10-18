package service;

import exception.InsufficientFundsException;
import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.Repository;
import util.StatePersister;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankServiceTest {

    @TempDir
    Path temp;

    private BankService service(Repository<Account> repository) {
        return new BankService(repository,
                temp.resolve("accounts.dat"),
                temp.resolve("logs"),
                temp.resolve("report.txt"));
    }

    @Test
    void depositIncreasesBalance() throws Exception {
        Repository<Account> repository = new Repository<>();
        Account account = new SavingsAccount("Riya", 1000, 0);
        repository.save(account);

        service(repository).deposit(account.getAccountNumber(), 500);

        assertEquals(1500, account.getBalance());
    }

    @Test
    void withdrawalOverSavingsLimitThrows() throws Exception {
        Repository<Account> repository = new Repository<>();
        Account account = new SavingsAccount("Riya", 1500, 1000);
        repository.save(account);
        BankService bank = service(repository);

        assertThrows(InsufficientFundsException.class,
                () -> bank.withdraw(account.getAccountNumber(), 1000));
        assertEquals(1500, account.getBalance());
    }

    @Test
    void concurrentDepositsKeepCorrectBalance() throws Exception {
        Repository<Account> repository = new Repository<>();
        Account account = new SavingsAccount("Riya", 0, 0);
        repository.save(account);

        service(repository).processConcurrentDeposits(
                account.getAccountNumber(), 1, 1000);

        assertEquals(1000, account.getBalance());
    }

    @Test
    void saveAndLoadPreservesAccountState() throws Exception {
        Account account = new SavingsAccount("Riya", 2500, 500);
        Path file = temp.resolve("accounts.dat");
        StatePersister.save(new Account[]{account}, file);

        Account[] loaded = StatePersister.load(file);

        assertEquals(1, loaded.length);
        assertEquals(account.getAccountNumber(), loaded[0].getAccountNumber());
        assertEquals(2500, loaded[0].getBalance());
    }

    @Test
    void oppositeDirectionTransfersRemainConsistent() throws Exception {
        Account first = new CurrentAccount("First", 100_000, 1000);
        Account second = new CurrentAccount("Second", 100_000, 1000);

        Thread one = new Thread(() -> transferMany(first, second));
        Thread two = new Thread(() -> transferMany(second, first));
        one.start();
        two.start();
        one.join();
        two.join();

        assertEquals(200_000, first.getBalance() + second.getBalance());
        assertEquals(100_000, first.getBalance());
        assertEquals(100_000, second.getBalance());
    }

    private static void transferMany(Account from, Account to) {
        for (int i = 0; i < 1000; i++) {
            try {
                from.transfer(to, 1);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }
}
