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




    }
}

