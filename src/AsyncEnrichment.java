import java.util.concurrent.*;
import java.util.function.Supplier;

public class AsyncEnrichment {

    record Partial(double rate, String fraud) {}
    record Enriched(double rate, String fraud, String customer) {}

    // ---- Simulated services ----
    static void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    static double exchangeRate()   { sleep(300); return 83.25; }
    static String fraudCheck()     { sleep(500); return "CLEAR"; }
    static String customerLookup() { sleep(200); return "Priya (Gold)"; }

    static String fraudCheckBroken() {
        sleep(100);
        throw new IllegalStateException("Fraud service unavailable");
    }

    // ---- Helpers ----
    static <T> T timed(String label, Supplier<T> action) {
        long start = System.nanoTime();
        T result = action.get();
        long ms = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("%-34s %5d ms   %s%n", label, ms, result);
        return result;
    }

    // Unwrap CompletionException / ExecutionException to get the real error
    static Throwable rootCause(Throwable t) {
        while ((t instanceof CompletionException || t instanceof ExecutionException)
                && t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }

    static Enriched combine(CompletableFuture<Double> rate,
                            CompletableFuture<String> fraud,
                            CompletableFuture<String> customer) {
        return rate.thenCombine(fraud, Partial::new)
                .thenCombine(customer, (p, c) -> new Enriched(p.rate(), p.fraud(), c))
                .join();
    }

    // ---- Part 1: sequential ----
    static void part1Sequential() {
        timed("1  sequential", () ->
                new Enriched(exchangeRate(), fraudCheck(), customerLookup()));
    }

    // ---- Part 2: concurrent ----
    static void part2Concurrent(ExecutorService pool) {
        timed("2a concurrent, thenCombine", () -> {
            var rate = CompletableFuture.supplyAsync(AsyncEnrichment::exchangeRate, pool);
            var fraud = CompletableFuture.supplyAsync(AsyncEnrichment::fraudCheck, pool);
            var cust = CompletableFuture.supplyAsync(AsyncEnrichment::customerLookup, pool);
            return combine(rate, fraud, cust);
        });

        timed("2b concurrent, allOf", () -> {
            var rate = CompletableFuture.supplyAsync(AsyncEnrichment::exchangeRate, pool);
            var fraud = CompletableFuture.supplyAsync(AsyncEnrichment::fraudCheck, pool);
            var cust = CompletableFuture.supplyAsync(AsyncEnrichment::customerLookup, pool);
            return CompletableFuture.allOf(rate, fraud, cust)
                    .thenApply(v -> new Enriched(rate.join(), fraud.join(), cust.join()))
                    .join();
        });
    }

    // ---- Part 3: one service fails, exceptionally() supplies a fallback ----
    static void part3Failure(ExecutorService pool) {
        timed("3  failure + exceptionally", () -> {
            var rate = CompletableFuture.supplyAsync(AsyncEnrichment::exchangeRate, pool);
            var fraud = CompletableFuture.supplyAsync(AsyncEnrichment::fraudCheckBroken, pool)
                    .exceptionally(ex -> {
                        System.err.println("  fraud fallback used: " + rootCause(ex).getMessage());
                        return "UNKNOWN";
                    });
            var cust = CompletableFuture.supplyAsync(AsyncEnrichment::customerLookup, pool);
            return combine(rate, fraud, cust);
        });

        // Same failure with NO handler: the whole pipeline fails
        try {
            var rate = CompletableFuture.supplyAsync(AsyncEnrichment::exchangeRate, pool);
            var fraud = CompletableFuture.supplyAsync(AsyncEnrichment::fraudCheckBroken, pool);
            var cust = CompletableFuture.supplyAsync(AsyncEnrichment::customerLookup, pool);
            combine(rate, fraud, cust);
        } catch (CompletionException e) {
            System.out.println("3b unhandled failure -> " + e.getClass().getSimpleName()
                    + " caused by " + e.getCause());
        }
    }

    // ---- Part 4: orTimeout(400 ms) against a 500 ms service ----
    static void part4Timeout(ExecutorService pool) {
        timed("4a orTimeout + exceptionally", () -> {
            var rate = CompletableFuture.supplyAsync(AsyncEnrichment::exchangeRate, pool);
            var fraud = CompletableFuture.supplyAsync(() -> {
                        String r = fraudCheck();
                        System.out.println("  (fraud task really finished later; result ignored)");
                        return r;
                    }, pool)
                    .orTimeout(400, TimeUnit.MILLISECONDS)
                    .exceptionally(ex -> rootCause(ex) instanceof TimeoutException
                            ? "TIMED_OUT" : "ERROR");
            var cust = CompletableFuture.supplyAsync(AsyncEnrichment::customerLookup, pool);
            return combine(rate, fraud, cust);
        });

        // Same timeout with no handler
        long start = System.nanoTime();
        try {
            var rate = CompletableFuture.supplyAsync(AsyncEnrichment::exchangeRate, pool);
            var fraud = CompletableFuture.supplyAsync(AsyncEnrichment::fraudCheck, pool)
                    .orTimeout(400, TimeUnit.MILLISECONDS);
            var cust = CompletableFuture.supplyAsync(AsyncEnrichment::customerLookup, pool);
            combine(rate, fraud, cust);
        } catch (CompletionException e) {
            long ms = (System.nanoTime() - start) / 1_000_000;
            System.out.printf("4b unhandled timeout after %d ms -> %s%n",
                    ms, e.getCause().getClass().getSimpleName());
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(6);
        try {
            part1Sequential();
            part2Concurrent(pool);
            part3Failure(pool);
            part4Timeout(pool);
        } finally {
            pool.shutdown();
            pool.awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}