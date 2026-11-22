public class UnsafeAccount implements BankOps {
    private long balance = 0;     // cents

    @Override
    public void deposit(long amount) {
        balance += amount;        // read, add, write: NOT atomic
    }

    @Override
    public long getBalance() { return balance; }
}