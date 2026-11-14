import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ParallelDemo {

    static List<BankAccount> generate(int count) {
        List<BankAccount> list = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            list.add(new BankAccount("Owner" + i, 1000.0 + (i % 5000), String.format("ACC%06d", i)));
        }
        return list;
    }

    // Warm up, then average several runs. Returns average nanoseconds per run.
    static long benchmark(String label, Supplier<Double> task) {
        final int WARMUP = 20, RUNS = 20;
        double result = 0;
        for (int i = 0; i < WARMUP; i++) result = task.get();

        long start = System.nanoTime();
        for (int i = 0; i < RUNS; i++) result = task.get();
        long avg = (System.nanoTime() - start) / RUNS;

        System.out.printf("%-28s %10d ns   result=%.2f%n", label, avg, result);
        return avg;
    }

    // Deliberately CPU-heavy work per element
    static double heavy(Account a) {
        double x = a.getBalance();
        for (int i = 0; i < 200; i++) {
            x = Math.sqrt(Math.abs(x) + i) + Math.sin(x);
        }
        return x;
    }

    public static void main(String[] args) {
        System.out.println("CPU cores: " + Runtime.getRuntime().availableProcessors());

        // ---- Test 1: cheap work (just adding balances), 100,000 accounts ----
        List<BankAccount> accounts = generate(100_000);
        System.out.println("\nTest 1: sum of 100,000 balances (cheap work)");
        long seq1 = benchmark("sequential", () ->
                accounts.stream().mapToDouble(Account::getBalance).sum());
        long par1 = benchmark("parallel", () ->
                accounts.parallelStream().mapToDouble(Account::getBalance).sum());
        System.out.println("Ratio sequential/parallel: " + String.format("%.2f", (double) seq1 / par1));

        // ---- Test 2: small list ----
        List<BankAccount> small = generate(100);
        System.out.println("\nTest 2: 100 accounts (tiny data)");
        long seq2 = benchmark("sequential", () ->
                small.stream().mapToDouble(Account::getBalance).sum());
        long par2 = benchmark("parallel", () ->
                small.parallelStream().mapToDouble(Account::getBalance).sum());
        System.out.println("Ratio sequential/parallel: " + String.format("%.2f", (double) seq2 / par2));

        // ---- Test 3: expensive work per element ----
        System.out.println("\nTest 3: 100,000 accounts, heavy work per element");
        long seq3 = benchmark("sequential", () ->
                accounts.stream().mapToDouble(ParallelDemo::heavy).sum());
        long par3 = benchmark("parallel", () ->
                accounts.parallelStream().mapToDouble(ParallelDemo::heavy).sum());
        System.out.println("Ratio sequential/parallel: " + String.format("%.2f", (double) seq3 / par3));
    }
}