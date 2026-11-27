import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class VirtualThreadDemo {
    static final int TASKS = 10_000;

    // Simulated slow external API call
    static String callApi(int id) throws InterruptedException {
        Thread.sleep(1000);
        return "response-" + id;
    }

    static void run(String label, ExecutorService executor) throws Exception {
        long start = System.nanoTime();
        int ok = 0, failed = 0;

        // try-with-resources: close() waits for all tasks (Java 19+)
        try (executor) {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < TASKS; i++) {
                final int id = i;
                futures.add(executor.submit(() -> callApi(id)));   // submit ALL first
            }
            for (Future<String> f : futures) {
                try { f.get(); ok++; }
                catch (ExecutionException e) { failed++; }
            }
        }

        long ms = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("%-26s %7d ms   ok=%d failed=%d%n", label, ms, ok, failed);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Java " + Runtime.version() + ", cores: "
                + Runtime.getRuntime().availableProcessors() + "\n");

        run("Virtual threads", Executors.newVirtualThreadPerTaskExecutor());
        run("Fixed pool (200)", Executors.newFixedThreadPool(200));   // takes ~50 s
    }
}