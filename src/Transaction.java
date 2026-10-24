public class Transaction implements Identifiable {
    private final int id;
    private final double amount;

    public Transaction(int id, double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        this.id = id;
        this.amount = amount;
    }

    @Override public int getId() { return id; }
    public double getAmount() { return amount; }

    @Override
    public String toString() { return "Transaction{id=" + id + ", amount=" + amount + "}"; }
}