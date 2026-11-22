public class SynchronizedAccount implements BankOps {
    private long balance = 0;

    @Override
    public synchronized void deposit(long amount) {
        balance += amount;
    }

    @Override
    public synchronized long getBalance() {   // reads need it too (visibility)
        return balance;
    }
}
