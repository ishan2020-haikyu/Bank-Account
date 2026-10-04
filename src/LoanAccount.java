public class LoanAccount extends Account {
    private final double creditLimit;

    public LoanAccount(String owner, double initialBalance, String accountNumber, double creditLimit) {
        super(owner, initialBalance, accountNumber);
        if (creditLimit < 0)
            throw new IllegalArgumentException("Credit limit cannot be negative");
        this.creditLimit = creditLimit;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0)
            throw new IllegalArgumentException("Withdrawal must be positive");
        if (getBalance() - amount < -creditLimit)
            throw new InsufficientFundsException(
                    "Cannot withdraw " + amount + ". Balance: " + getBalance()
                            + ", credit limit: " + creditLimit);
        adjustBalance(-amount);
    }

    public double getCreditLimit() { return creditLimit; }

    @Override
    public String getAccountType() { return "Loan Account"; }
}