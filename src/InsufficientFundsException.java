public class InsufficientFundsException extends BankingException {
    private final double available;
    private final double requested;

    public InsufficientFundsException(double available, double requested) {
        super(String.format("Insufficient funds: requested %.2f, available %.2f, short by %.2f",
                requested, available, requested - available));
        this.available = available;
        this.requested = requested;
    }

    public double getAvailable() { return available; }
    public double getRequested() { return requested; }
    public double getShortfall() { return requested - available; }
}