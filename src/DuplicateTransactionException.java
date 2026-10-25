public class DuplicateTransactionException extends RuntimeException {
    private final String transactionId;

    public DuplicateTransactionException(String transactionId) {
        super("Duplicate transaction id: " + transactionId);
        this.transactionId = transactionId;
    }

    public String getTransactionId() { return transactionId; }
}