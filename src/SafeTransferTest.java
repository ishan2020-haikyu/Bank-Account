import java.util.ArrayList;
import java.util.List;

public class SafeTransferTest {
    public static void main(String[] args) throws Exception {
        Account a = new BankAccount("Priya", 1_000_000.0, "ACC-A");
        Account b = new BankAccount("Gopi", 1_000_000.0, "ACC-B");
        double before = a.getBalance() + b.getBalance();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            boolean forward = (i % 2 == 0);
            threads.add(new Thread(() -> {
                for (int n = 0; n < 10_000; n++) {
                    try {
                        if (forward) Account.transfer(a, b, 1);
                        else         Account.transfer(b, a, 1);
                    } catch (InsufficientFundsException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }, "worker-" + i));
        }

        threads.forEach(Thread::start);
        boolean finished = true;
        for (Thread t : threads) {
            t.join(10_000);
            if (t.isAlive()) finished = false;
        }

        System.out.println("All threads finished: " + finished);
        System.out.println("Total before: " + before);
        System.out.println("Total after:  " + (a.getBalance() + b.getBalance()));
    }
}