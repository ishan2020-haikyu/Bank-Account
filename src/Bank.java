import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public Optional<Account> findAccountById(String id) {
        if (id == null || id.isBlank())
            return Optional.empty();
        return Optional.ofNullable(accounts.get(id));
    }

    public Account findAccount(String id) throws AccountNotFoundException {
        return findAccountById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public int size() { return accounts.size(); }
}