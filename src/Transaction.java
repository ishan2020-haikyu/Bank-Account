import java.time.LocalDate;

public class Transaction implements Identifiable {
    private final int id;
    private final String accountId;
    private final TransactionType type;
    private final double amount;
    private final LocalDate date;

    public Transaction(int id, String accountId, TransactionType type,
                       double amount, LocalDate date) {
        if (accountId == null || accountId.isBlank())
            throw new IllegalArgumentException("Account id is required");
        if (type == null)
            throw new IllegalArgumentException("Type is required");
        if (date == null)
            throw new IllegalArgumentException("Date is required");
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0)
            throw new InvalidAmountException(amount);
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public int getId() { return id; }

    public String getAccountId() { return accountId; }

    public TransactionType getType() { return type; }

    public double getAmount() { return amount; }

    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", account=" + accountId + ", type=" + type
                + ", amount=" + amount + ", date=" + date + "}";
    }
}