public class TransactionPersistenceException extends BankingException {
    public TransactionPersistenceException(String message, Throwable cause) {
        super(message, cause);   // super(message, cause) stores the cause
    }
}