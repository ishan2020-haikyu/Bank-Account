import java.util.*;

public class Main {

    public static void printAccountSummary(Account acc) {   // Account, not BankAccount
        System.out.println("Type:           " + acc.getAccountType());
        System.out.println("Owner:          " + acc.getOwner());
        System.out.println("Account Number: " + acc.getAccountNumber());
        System.out.println("Balance:        " + acc.getBalance());
        System.out.println("----------------------------");
    }

    public static void main(String[] args) {
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
    }
}

