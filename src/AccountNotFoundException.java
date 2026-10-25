
public class AccountNotFoundException extends BankingException {
    private final String accountId;

    public AccountNotFoundException(String accountId) {
        super("No account found with id: " + accountId);
        this.accountId = accountId;
    }

    public String getAccountId() { return accountId; }
}