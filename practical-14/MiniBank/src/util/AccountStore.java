package util;
import model.Account;import java.util.*;
public class AccountStore {
    private final HashMap<String,Account> accounts=new HashMap<>();private final ArrayList<String> history=new ArrayList<>();
    public Account add(Account account){if(account==null)throw new IllegalArgumentException("Account is required");accounts.put(account.getAccountNumber(),account);return account;}
    public Account get(String accountNumber){return accounts.get(accountNumber);}
    public ArrayList<Account> getAll(){return new ArrayList<>(accounts.values());}
    public ArrayList<Account> sortedByBalance(){ArrayList<Account> sorted=getAll();sorted.sort(Comparator.comparingLong(Account::getBalance).reversed().thenComparing(Account::getAccountNumber));return sorted;}
    public void record(String transaction){history.add(transaction);}
    public ArrayList<String> getHistory(){return new ArrayList<>(history);}
}
