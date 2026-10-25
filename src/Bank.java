import java.util.HashMap;
import java.util.Map;

public class Bank {
    private final Map<String, Account> accounts = new HashMap<>();

    public void addAccount(Account account) {
        if (account == null)
            throw new IllegalArgumentException("Account is required");
        String number = account.getAccountNumber();
        if (accounts.containsKey(number))
            throw new IllegalArgumentException("Duplicate account number: " + number);
        accounts.put(number, account);
    }

    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        if (accountNumber == null || accountNumber.isBlank())
            throw new AccountNotFoundException(accountNumber);

        Account account = accounts.get(accountNumber);
        if (account == null)
            throw new AccountNotFoundException("No account found with number: " + accountNumber);
        return account;
    }

    public Account findAccountOrDefault(String accountNumber, Account fallback) {
        return accounts.getOrDefault(accountNumber, fallback);
    }
}