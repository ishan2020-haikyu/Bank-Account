public abstract class Account {

    private final String owner;
    private final String accountNumber;
    private double balance;

    protected Account(String owner, double initialBalance, String accountNumber) {
        if (owner == null || owner.isBlank())
            throw new IllegalArgumentException("Owner is required");
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Account number is required");
        if (initialBalance < 0)
            throw new IllegalArgumentException("Initial balance cannot be negative");
        this.owner = owner;
        this.balance = initialBalance;
        this.accountNumber = accountNumber;
    }

    public void deposit(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Deposit must be positive");
        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0)
            throw new IllegalArgumentException("Withdrawal must be positive");
        if (amount > balance)
            throw new InsufficientFundsException(
                    "Cannot withdraw " + amount + ". Available: " + balance);
        balance -= amount;
    }

    public double getBalance() { return balance; }
    public String getOwner() { return owner; }
    public String getAccountNumber() { return accountNumber; }

    protected void adjustBalance(double delta) { balance += delta; }

    // Every subclass MUST provide this
    public abstract String getAccountType();

}