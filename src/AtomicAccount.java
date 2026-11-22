import java.util.concurrent.atomic.AtomicLong;

public class AtomicAccount implements BankOps {
    private final AtomicLong balanceCents = new AtomicLong(0);

    @Override
    public void deposit(long cents) {
        balanceCents.addAndGet(cents);    // one atomic hardware-level operation
    }

    @Override
    public long getBalance() { return balanceCents.get(); }
}