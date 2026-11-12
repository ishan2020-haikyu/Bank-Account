import java.sql.SQLException;

public abstract class Account {

    private final String owner;
    private final String accountNumber;
    private double balance;
    private boolean active = true;     // new accounts start active

    public boolean isActive() { return active; }
    public void deactivate() { active = false; }
    public void activate() { active = true; }

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

    public boolean processTransaction(TransactionType type, double amount)
            throws TransactionPersistenceException {
        try {
            switch (type) {
                case CREDIT -> deposit(amount);
                case DEBIT  -> withdraw(amount);
                default     -> throw new UnsupportedOperationException("Unsupported type: " + type);
            }
            saveToDatabase(type, amount);          // can throw SQLException
            return true;

        } catch (SQLException e) {
            // Wrap and rethrow, passing the original as the cause
            throw new TransactionPersistenceException("DB write failed", e);

        } catch (InsufficientFundsException e) {
            System.err.println("[FAILED] " + e.getMessage());
        } catch (InvalidAmountException e) {
            System.err.println("[INVALID] " + e.getMessage());
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.err.println("[BAD REQUEST] " + e.getMessage());
        }
        return false;
    }

    public double getBalance() { return balance; }
    public String getOwner() { return owner; }
    public String getAccountNumber() { return accountNumber; }

    protected void adjustBalance(double delta) { balance += delta; }

    // Every subclass MUST provide this
    public abstract String getAccountType();

    // In Account (or a small TransactionRepository)
    protected void saveToDatabase(TransactionType type, double amount) throws SQLException {
        // Simulated failure, as if the connection dropped
        throw new SQLException("Connection refused: db-host:5432", "08001", 1042);
    }

    @Override
    public String toString() {
        return getOwner() + " (" + getAccountType() + ", " + getBalance() + ")";
    }

}