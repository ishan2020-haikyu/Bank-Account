public interface Transferable {
    void transfer(Account to, double amount) throws InsufficientFundsException;
}