import java.util.concurrent.locks.ReentrantLock;

public class LockAccount implements BankOps {
    private final ReentrantLock lock = new ReentrantLock();
    private long balance = 0;

    @Override
    public void deposit(long amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();            // ALWAYS in finally
        }
    }

    @Override
    public long getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}