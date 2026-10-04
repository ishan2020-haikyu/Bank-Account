public class BankAccount extends Account {
    public BankAccount(String owner, double initialBalance, String accountNumber) {
        super(owner, initialBalance, accountNumber);
    }

    @Override
    public String getAccountType() { return "Standard Account"; }
}