public interface BankOps {
    void deposit(long amount);
    long getBalance();      // in cents
}