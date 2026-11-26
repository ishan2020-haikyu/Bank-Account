import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BatchProcessor {

    static List<Transaction> generate(int count) {
        List<Transaction> list = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            list.add(new Transaction(i, "ACC" + (i % 10),
                    i % 2 == 0 ? TransactionType.CREDIT : TransactionType.DEBIT,
                    10.0 + i, LocalDate.now()));
        }
        return list;
    }

    // One unit of work: simulated DB call. Every 10th transaction fails.
    static int process(Transaction t) throws Exception {
        Thread.sleep(50);
        if (t.getId() % 10 == 0)
            throw new TransactionPersistenceException(
                    "DB write failed for transaction " + t.getId(),
                    new SQLException("Simulated timeout"));
        return t.getId();
    }

    static void runSequential(List<Transaction> txns) {
        int ok = 0, failed = 0;
        long start = System.nanoTime();
        for (Transaction t : txns) {
            try {
                process(t);
                ok++;
            } catch (Exception e) {
                failed++;
            }
        }
        long ms = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Sequential:  %6d ms   succeeded=%d failed=%d%n", ms, ok, failed);
    }

    static void runParallel(List<Transaction> txns) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<Integer>> futures = new ArrayList<>();
        int ok = 0, failed = 0;
        long start = System.nanoTime();

        try {
            // Phase 1: submit EVERYTHING first (does not block)
            for (Transaction t : txns) {
                futures.add(pool.submit(() -> process(t)));
            }

            // Phase 2: collect the results
            for (Future<Integer> f : futures) {
                try {
                    f.get();                       // blocks until this task is done
                    ok++;
                } catch (ExecutionException e) {   // the task threw; the real error is the cause
                    failed++;
                    if (failed <= 3)               // print a few samples, not all 100
                        System.err.println("Task failed: " + e.getCause().getMessage());
                }
            }
        } finally {
            shutdownProperly(pool);
        }

        long ms = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Pool of 8:   %6d ms   succeeded=%d failed=%d%n", ms, ok, failed);
    }

    static void shutdownProperly(ExecutorService pool) throws InterruptedException {
        pool.shutdown();                                       // stop accepting new tasks
        if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {    // wait for running tasks
            pool.shutdownNow();                                // interrupt the stragglers
            if (!pool.awaitTermination(10, TimeUnit.SECONDS))
                System.err.println("Pool did not terminate");
        }
        System.out.println("Pool terminated: " + pool.isTerminated());
    }

    public static void main(String[] args) throws Exception {
        List<Transaction> txns = generate(1_000);
        System.out.println("Generated " + txns.size() + " transactions (sequential takes ~50 s)\n");
        runSequential(txns);
        runParallel(txns);
    }
}