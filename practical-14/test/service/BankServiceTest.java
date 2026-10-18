package service;

import exception.InsufficientFundsException;
import model.Account;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.Repository;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class BankServiceTest {
    @TempDir Path temp;
    private BankService service(Repository<Account> repo) {
        return new BankService(repo, temp.resolve("accounts.dat"), temp.resolve("logs"), temp.resolve("report.txt"));
    }
    @Test void depositIncreasesBalance() throws Exception {
        Repository<Account> repo = new Repository<>();
        Account account = new SavingsAccount("Test", 1000, 0);
        repo.save(account);
        service(repo).deposit(account.getAccountNumber(), 500);
        assertEquals(1500, account.getBalance());
    }
    @Test void withdrawalOverSavingsLimitThrows() throws Exception {
        Repository<Account> repo = new Repository<>();
        Account account = new SavingsAccount("Test", 1500, 1000);
        repo.save(account);
        BankService bank = service(repo);
        assertThrows(InsufficientFundsException.class, () -> bank.withdraw(account.getAccountNumber(), 1000));
        assertEquals(1500, account.getBalance());
    }
    @Test void concurrentDepositsKeepCorrectBalance() throws Exception {
        Repository<Account> repo = new Repository<>();
        Account account = new SavingsAccount("Test", 0, 0);
        repo.save(account);
        service(repo).processConcurrentDeposits(account.getAccountNumber(), 1, 1000);
        assertEquals(1000, account.getBalance());
    }
}
