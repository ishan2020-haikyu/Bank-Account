import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void printAccountSummary(Account acc) {   // Account, not BankAccount
        System.out.println("Type:           " + acc.getAccountType());
        System.out.println("Owner:          " + acc.getOwner());
        System.out.println("Account Number: " + acc.getAccountNumber());
        System.out.println("Balance:        " + acc.getBalance());
        System.out.println("----------------------------");
    }

    // Linear search: checks elements one by one, O(n)
    static Account findInList(List<Account> list, String accountNumber) throws AccountNotFoundException {
        for (Account acc : list) {
            if (acc.getAccountNumber().equals(accountNumber)) return acc;
        }
        throw new AccountNotFoundException("No account found with number: " + accountNumber);
    }

    static void addTransaction(Map<String, List<Double>> map, String type, double amount) {
        map.computeIfAbsent(type, k -> new ArrayList<>()).add(amount);
    }

    public static void main(String[] args) throws AccountNotFoundException {
        // Exercise 1: BankAccount basics
        BankAccount basic = new BankAccount("Asha", 2000.0, "ACC001");
        basic.deposit(500);
        System.out.println("Ex1 balance: " + basic.getBalance());       // 2500.0
        try {
            basic.withdraw(5000);
        } catch (InsufficientFundsException e) {
            System.out.println("Ex1 caught: " + e.getMessage());
        }

        // Exercise 2: Savings interest and Loan credit limit
        SavingsAccount sav = new SavingsAccount("Raj", 10000.0, "SAV001", 0.05);
        sav.applyInterest();
        System.out.println("Ex2 savings: " + sav.getBalance());          // 10500.0

        LoanAccount loan = new LoanAccount("Romeo", 5000.0, "LON001", 20000.0);
        try {
            loan.withdraw(15000.0);
            System.out.println("Ex2 loan: " + loan.getBalance());        // -10000.0
            loan.withdraw(15000.0);                                      // over the limit
        } catch (InsufficientFundsException e) {
            System.out.println("Ex2 caught: " + e.getMessage());
        }
        System.out.println("Ex2 loan after failure: " + loan.getBalance()); // -10000.0

        // Exercise 3: polymorphism, list type is now Account
        List<Account> accounts = new ArrayList<>();
        accounts.add(basic);
        accounts.add(sav);
        accounts.add(loan);
        for (Account acc : accounts) {
            printAccountSummary(acc);
        }

        // Exercise 4: transfer
        SavingsAccount a = new SavingsAccount("Raj", 10000.0, "SAV002", 0.05);
        SavingsAccount b = new SavingsAccount("Asha", 5000.0, "SAV003", 0.05);
        try {
            a.transfer(b, 3000.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
        System.out.println("Ex4: A = " + a.getBalance() + ", B = " + b.getBalance()); // 7000.0, 8000.0

        // Exercise 5: Account is abstract, so this must NOT compile.
        // Uncomment to see the error, then comment it out again:
        // Account bad = new Account("X", 0, "Y");

//        Exercise 1 — Store accounts in a List
//        Create an ArrayList with at least 5 accounts.
//        Add, remove, and retrieve by index. Sort the list by balance using Collections.sort() with a Comparator. Print all accounts before and after sorting.
//        Then make the list unmodifiable and verify it throws an exception.

        System.out.println("-------------- Exercise 3.1 Solutions --------------");

        List<Account> accountList = new ArrayList<>();
        accountList.add(new BankAccount("Priya", 5000.0, "SBI001122"));
        accountList.add(new SavingsAccount("Gopi", 1200.0, "SBI002233", 0.0535));
        accountList.add(new LoanAccount("Riya", 4500.0, "HDFC112233", 20000.0));
        accountList.add(new BankAccount("Sanju", 5500.0, "SBI00123456"));
        accountList.add(new SavingsAccount("Mumpu", 8200.0, "SBI00223377", 0.0735));

// Add / retrieve / remove
        Account first = accountList.get(0);
        accountList.add(new BankAccount("Temp", 100.0, "TMP001"));
        Account removed = accountList.remove(accountList.size() - 1);
        System.out.println("Removed: " + removed.getOwner());
        try {
            accountList.get(99);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }

// Before sorting
        System.out.println("---- Before sorting ----");
        accountList.forEach(Main::printAccountSummary);

// Sort
        Collections.sort(accountList, Comparator.comparingDouble(Account::getBalance));

        System.out.println("---- After sorting ----");
        accountList.forEach(Main::printAccountSummary);

// Unmodifiable
        List<Account> readOnly = Collections.unmodifiableList(accountList);
        try {
            readOnly.add(new BankAccount("Pune", 1500.0, "HDFC00115599"));
            System.out.println("ERROR: add should have failed!");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught as expected: UnsupportedOperationException");
        }

//        Exercise 2 — Deduplicate with a Set
//        Generate a List of 10 transaction IDs where some are duplicates. Convert it to a HashSet to remove
//        duplicates. Print the size before and after. Then try with a TreeSet and observe the IDs come out sorted.
//        Override equals() and hashCode() on a custom TransactionId class.

        System.out.println("-------------- Exercise 03. Solutions --------------");

        List<String> transactionIds = new ArrayList<>(List.of(
                "TXN1001",
                "TXN1002",
                "TXN1003",
                "TXN1001",   // duplicate of 1st
                "TXN1004",
                "TXN1003",   // duplicate of 3rd
                "TXN1005",
                "TXN1003",   // duplicate again (3rd occurrence)
                "TXN1006",
                "TXN1005"    // duplicate of 7th
        ));

        System.out.println("Size Before uniqueness - " + transactionIds.size());

        LinkedHashSet<String> uniqueTransactionIds = new LinkedHashSet<>(transactionIds);
        System.out.println("Size After uniqueness - " + uniqueTransactionIds.size());

        TreeSet<String> sortedTransactionIds = new TreeSet<>(uniqueTransactionIds);
        System.out.println("Sorted unique transaction ids - " + sortedTransactionIds);

        //Listings transactions !
        TransactionId aTran = new TransactionId("TXN1001");
        TransactionId bTran = new TransactionId("TXN1001");
        TransactionId cTran = new TransactionId("txn1001");   // normalised, so equal too
        TransactionId dTran = new TransactionId("TXN1002");

        System.out.println(aTran == bTran);                        // false (different objects)
        System.out.println(aTran.equals(bTran));                   // true
        System.out.println(aTran.equals(cTran));                   // true
        System.out.println(aTran.equals(dTran));                   // false
        System.out.println(aTran.hashCode() == bTran.hashCode());  // true

        List<TransactionId> ids = new ArrayList<>(List.of(
                new TransactionId("TXN1001"), new TransactionId("TXN1002"),
                new TransactionId("TXN1003"), new TransactionId("TXN1001"),
                new TransactionId("TXN1004"), new TransactionId("TXN1003"),
                new TransactionId("TXN1005"), new TransactionId("TXN1003"),
                new TransactionId("TXN1006"), new TransactionId("TXN1005")
        ));

        Set<TransactionId> unique = new HashSet<>(ids);
        System.out.println("Original: " + ids.size());     // 10
        System.out.println("Unique:   " + unique.size());  // 6
        System.out.println(unique);

//        Exercise 3 — Build an account lookup Map
//        Create a HashMap using account numbers as keys. Implement a findAccount(String accountNumber) method that returns the account or throws AccountNotFoundException if not found.
//        Use getOrDefault() and containsKey(). Time how long it takes to find account 10000 in a list vs a map (use System.nanoTime()).

        System.out.println("-------------- Exercise 3.3 Solutions --------------");

        Map<String, Account> accountMap = new HashMap<>();

        Account priya = new BankAccount("Priya", 5000.0, "SBI001122");
        Account gopi  = new SavingsAccount("Gopi", 1200.0, "SBI002233", 0.0535);
        Account riya  = new LoanAccount("Riya", 4500.0, "HDFC112233", 20000.0);

        // Add: key = account number, value = the account
        accountMap.put(priya.getAccountNumber(), priya);
        accountMap.put(gopi.getAccountNumber(), gopi);
        accountMap.put(riya.getAccountNumber(), riya);

        // Retrieve by key
        Account found = accountMap.get("SBI002233");
        System.out.println(found.getOwner());               // Gopi

        // Missing key returns null
        System.out.println(accountMap.get("NOPE"));         // Null

        // Check existence
        System.out.println(accountMap.containsKey("SBI001122"));   // True

        // Remove by key
        accountMap.remove("HDFC112233");
        System.out.println(accountMap.size());              // 2

        // Loop over entries
        for (Map.Entry<String, Account> entry : accountMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue().getOwner());
        }

        final int COUNT = 20_000;
        List<Account> list = new ArrayList<>();
        Bank bank = new Bank();

        for (int i = 1; i <= COUNT; i++) {
            Account acc = new BankAccount("Owner" + i, 1000.0, String.format("ACC%05d", i));
            list.add(acc);
            bank.addAccount(acc);
        }

        String target = "ACC10000";

        // Warm-up: lets the JVM optimise the code before we measure
        for (int i = 0; i < 1_000; i++) {
            findInList(list, target);
            bank.findAccount(target);
        }

        // Single timing (as the exercise asks)
        long start = System.nanoTime();
        findInList(list, target);
        long listTime = System.nanoTime() - start;

        start = System.nanoTime();
        bank.findAccount(target);
        long mapTime = System.nanoTime() - start;

        System.out.println("List lookup: " + listTime + " ns");
        System.out.println("Map lookup:  " + mapTime + " ns");

        // Average over many runs (more reliable)
        final int RUNS = 1_000;
        start = System.nanoTime();
        for (int i = 0; i < RUNS; i++) findInList(list, target);
        long listAvg = (System.nanoTime() - start) / RUNS;

        start = System.nanoTime();
        for (int i = 0; i < RUNS; i++) bank.findAccount(target);
        long mapAvg = (System.nanoTime() - start) / RUNS;

        System.out.println("List average: " + listAvg + " ns");
        System.out.println("Map average:  " + mapAvg + " ns");
        System.out.println("Map is about " + (listAvg / Math.max(mapAvg, 1)) + "x faster");

        // Not-found and getOrDefault demos
        try {
            bank.findAccount("NOPE999");
        } catch (AccountNotFoundException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println(bank.findAccountOrDefault("NOPE999", null)); // null, no exception

//        Exercise 4 — Group transactions by type
//        Create a <Map> where the key is transaction type (CREDIT, DEBIT, TRANSFER) and the value is a list of amounts. Add 10 sample transactions.
//        Then iterate the map and print the total amount per type. This pattern — grouping data by a key — is used everywhere in Fintech.

        System.out.println("-------------- Exercise 3.4 Solutions --------------");

        Map<String, List<Double>> transactions = new HashMap<>();

        addTransaction(transactions, "CREDIT", 5000.0);
        addTransaction(transactions, "DEBIT", 1200.0);
        addTransaction(transactions, "CREDIT", 2500.0);
        addTransaction(transactions, "TRANSFER", 3000.0);
        addTransaction(transactions, "DEBIT", 450.5);
        addTransaction(transactions, "CREDIT", 800.0);
        addTransaction(transactions, "TRANSFER", 1500.0);
        addTransaction(transactions, "DEBIT", 99.5);
        addTransaction(transactions, "CREDIT", 10000.0);
        addTransaction(transactions, "TRANSFER", 750.0);

        for (Map.Entry<String, List<Double>> entry : transactions.entrySet()) {
            double total = 0;
            for (double amount : entry.getValue()) {
                total += amount;
            }
            System.out.println(entry.getKey() + ": " + entry.getValue()
                    + " -> total = " + total);
        }

        Map<TransactionType, List<Double>> transactions01 = new EnumMap<>(TransactionType.class);

        transactions01.computeIfAbsent(TransactionType.CREDIT, k -> new ArrayList<>()).add(5000.0);

        transactions01.forEach((type, amounts) -> {
            double total = amounts.stream().mapToDouble(Double::doubleValue).sum();
            System.out.println(type + " -> " + total);
        });

        System.out.println("-------------- Exercise 3.5 Solutions --------------");
        // Repository of Customers
        Repository<Customer> customers = new Repository<>();
        customers.add(new Customer(1, "Priya"));
        customers.add(new Customer(2, "Gopi"));

        System.out.println(customers.getAll());
        System.out.println(customers.findById(2).map(Customer::getName).orElse("not found")); // Gopi
        System.out.println(customers.findById(99).isPresent());                                // false

        // Repository of Transactions
        Repository<Transaction> txns = new Repository<>();
        txns.add(new Transaction(101, 2500.0));
        txns.add(new Transaction(102, 99.5));

        System.out.println(txns.getAll());
        System.out.println(txns.findById(101).get().getAmount());                              // 2500.0

        // Duplicate id must fail
        try {
            txns.add(new Transaction(101, 10.0));
            System.out.println("ERROR: should have thrown!");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // getAll() must be read-only
        try {
            customers.getAll().clear();
            System.out.println("ERROR: should have thrown!");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught: list is read-only");
        }

        // Compile-time safety: this line would NOT compile
        // customers.add(new Transaction(5, 10.0));   // wrong type for Repository<Customer>


        System.out.println("-------------- Exercise 4.1 Solutions --------------");

        BankAccount acc = new BankAccount("Priya", 1000.0, "SBI001");

        // Checked: the compiler forces you to handle it
        try {
            acc.withdraw(1500.0);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
            System.out.println("Shortfall: " + e.getShortfall());     // 500.0
        }

        // Unchecked: no try/catch required, but you can still catch it
        try {
            acc.deposit(-50);
        } catch (InvalidAmountException e) {
            System.out.println(e.getMessage() + " | amount=" + e.getAmount());
        }

        // One catch for the whole checked family
        try {
            Bank bank01 = new Bank();
            bank01.findAccount("NOPE");
        } catch (BankingException e) {                 // catches both checked types
            System.out.println("Banking error: " + e.getMessage());
            if (e instanceof AccountNotFoundException anf) {
                System.out.println("Missing id: " + anf.getAccountId());
            }
        }

        // Unchecked duplicate
        try {
            throw new DuplicateTransactionException("TXN1001");
        } catch (DuplicateTransactionException e) {
            System.out.println(e.getTransactionId());
        }

        System.out.println("-------------- Exercise 4.2 Solutions --------------");

        acc.processTransaction(TransactionType.CREDIT, 500.0);          // success
        acc.processTransaction(TransactionType.DEBIT, 200.0);           // success
        acc.processTransaction(TransactionType.CREDIT, -50.0);          // negative deposit
        acc.processTransaction(TransactionType.CREDIT, 0.0);            // zero deposit
        acc.processTransaction(TransactionType.DEBIT, -10.0);           // negative withdrawal
        acc.processTransaction(TransactionType.DEBIT, 0.0);             // zero withdrawal
        acc.processTransaction(TransactionType.DEBIT, 5000.0);          // overdraft
        acc.processTransaction(TransactionType.CREDIT, Double.NaN);     // NaN
        acc.processTransaction(null, 100.0);                            // null type
        acc.processTransaction(TransactionType.TRANSFER, 100.0);        // unsupported type

        System.out.println("Final balance: " + acc.getBalance());       // must still be 1300.0

        // Loan boundary: balance 1000, limit 2000 -> can go down to exactly -2000
        LoanAccount loan01 = new LoanAccount("Riya", 1000.0, "LON001", 2000.0);
        loan01.processTransaction(TransactionType.DEBIT, 3000.0);         // success, balance -2000.0
        loan01.processTransaction(TransactionType.DEBIT, 1.0);            // fails, 1.00 over the limit
        System.out.println("Loan balance: " + loan01.getBalance());       // -2000.0


        System.out.println("-------------- Exercise 4.3 Solutions --------------");
        // Test 1: normal use
        try (TransactionLogger logger = new TransactionLogger("audit.log")) {
            logger.log("CREDIT 5000.0 account=SBI001");
            logger.log("DEBIT 1200.0 account=SBI001");
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }

        // Test 2: file exists and has content
        File file = new File("audit.log");
        System.out.println("Exists: " + file.exists() + ", size: " + file.length() + " bytes");

        // Test 3: close() still runs when an exception happens inside the block
        try (TransactionLogger logger = new TransactionLogger("audit.log")) {
            logger.log("DEBIT 9999.0 account=SBI001 (about to fail)");
            throw new IllegalStateException("Simulated failure inside the block");
        } catch (IllegalStateException e) {
            System.err.println("Caught: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }

        // Test 4: append mode kept the earlier entries
        try (var reader = new java.io.BufferedReader(new java.io.FileReader("audit.log"))) {
            reader.lines().forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Read failed: " + e.getMessage());
        }

        System.out.println("-------------- Exercise 4.4 Solutions --------------");

        Bank bank02 = new Bank();
        bank02.addAccount(new BankAccount("Priya", 1000.0, "SBI001"));
        bank02.addAccount(new BankAccount("Gopi", 2000.0, "SBI002"));

        TransactionProcessor processor = new TransactionProcessor(bank02);
        processor.processFile("transactions.csv");

        System.out.println("Processed: " + processor.getProcessed());
        System.out.println("Failed:    " + processor.getFailed());

        try {
            System.out.println("SBI001 balance: " + bank02.findAccount("SBI001").getBalance());
            System.out.println("SBI002 balance: " + bank02.findAccount("SBI002").getBalance());
        } catch (AccountNotFoundException e) {
            System.err.println(e.getMessage());
        }

        // File-not-found path: must log and return, not crash
        new TransactionProcessor(bank02).processFile("missing.csv");

    }
}

