import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class DeadlockDemo {
    public static void main(String[] args) throws Exception {
        Account a = new BankAccount("Priya", 1000.0, "ACC-A");
        Account b = new BankAccount("Gopi", 1000.0, "ACC-B");

        Thread t1 = new Thread(() -> {
            try { Account.transferNaive(a, b, 100); }
            catch (InsufficientFundsException e) { System.err.println(e.getMessage()); }
        }, "T1-A-to-B");

        Thread t2 = new Thread(() -> {
            try { Account.transferNaive(b, a, 50); }
            catch (InsufficientFundsException e) { System.err.println(e.getMessage()); }
        }, "T2-B-to-A");

        t1.start();
        t2.start();

        t1.join(2000);                   // wait at most 2 seconds
        t2.join(2000);

        if (t1.isAlive() || t2.isAlive()) {
            System.out.println("DEADLOCK suspected: threads still alive after 2s");
            reportDeadlock();
            System.out.println("Run 'jps' then 'jstack <pid>' now. Press Ctrl+C to stop.");
            t1.join();                   // stay hung so you can take a thread dump
        } else {
            System.out.println("Finished without deadlock (unlucky timing)");
        }
    }

    static void reportDeadlock() {
        ThreadMXBean mx = ManagementFactory.getThreadMXBean();
        long[] ids = mx.findDeadlockedThreads();       // null if none
        if (ids == null) { System.out.println("MXBean: no deadlock found"); return; }
        for (ThreadInfo info : mx.getThreadInfo(ids)) {
            System.out.println(info.getThreadName() + " is waiting for " + info.getLockName()
                    + " held by " + info.getLockOwnerName());
        }
    }
}