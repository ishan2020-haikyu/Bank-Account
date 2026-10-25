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
        validateAmount(amount);
        balance += amount;
    }

    protected static void validateAmount(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0)
            throw new InvalidAmountException(amount);
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        validateAmount(amount);
        if (amount > balance)
            throw new InsufficientFundsException(balance, amount);
        balance -= amount;
    }

    public boolean processTransaction(TransactionType type, double amount) {
        try {
            if (type == null)
                throw new IllegalArgumentException("Transaction type is required");

            switch (type) {
                case CREDIT -> deposit(amount);
                case DEBIT  -> withdraw(amount);
                default     -> throw new UnsupportedOperationException("Unsupported type: " + type);
            }
            System.out.println("OK " + type + " " + amount + " -> balance " + getBalance());
            return true;

        } catch (InsufficientFundsException e) {                 // checked, most specific first
            System.err.println("[FAILED] " + e.getMessage());
        } catch (InvalidAmountException e) {                     // must come BEFORE IllegalArgumentException
            System.err.println("[INVALID] " + e.getMessage());
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.err.println("[BAD REQUEST] " + e.getMessage());
        }
        return false;                                            // reached only after a failure
    }

    public double getBalance() { return balance; }
    public String getOwner() { return owner; }
    public String getAccountNumber() { return accountNumber; }

    protected void adjustBalance(double delta) { balance += delta; }

    // Every subclass MUST provide this
    public abstract String getAccountType();

}