public class InvalidAmountException extends IllegalArgumentException {
    private final double amount;

    public InvalidAmountException(double amount) {
        super("Invalid amount: " + amount + " (must be greater than zero)");
        this.amount = amount;
    }

    public double getAmount() { return amount; }
}