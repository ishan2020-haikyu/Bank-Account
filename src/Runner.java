import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Runner {
    static final int THREADS = 10;
    static final int DEPOSITS_PER_THREAD = 1_000;
    static final long EXPECTED = (long) THREADS * DEPOSITS_PER_THREAD;   // 10,000

    // Runs one trial and returns the final balance
    static long runOnce(Supplier<BankOps> factory) throws InterruptedException {
        BankOps account = factory.get();
        List<Thread> threads = new ArrayList<>();

        for (int t = 0; t < THREADS; t++) {
            Thread thread = new Thread(() -> {
                for (int i = 0; i < DEPOSITS_PER_THREAD; i++) {
                    account.deposit(1);
                }
            });
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (Thread thread : threads) thread.join();   // wait for all, THEN read the balance

        return account.getBalance();
    }

    // Runs 20 trials and reports
    static void runTrials(String label, Supplier<BankOps> factory) throws InterruptedException {
        final int TRIALS = 20;
        int correct = 0;
        long min = Long.MAX_VALUE, max = Long.MIN_VALUE;

        for (int i = 0; i < TRIALS; i++) {
            long result = runOnce(factory);
            if (result == EXPECTED) correct++;
            min = Math.min(min, result);
            max = Math.max(max, result);
        }
        System.out.printf("%-16s correct %2d/%d   min=%d max=%d%n",
                label, correct, TRIALS, min, max);
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Expected balance: " + EXPECTED + "\n");
        runTrials("Unsafe",        UnsafeAccount::new);
        runTrials("synchronized",  SynchronizedAccount::new);
        runTrials("AtomicLong",    AtomicAccount::new);
        runTrials("ReentrantLock", LockAccount::new);
    }
}