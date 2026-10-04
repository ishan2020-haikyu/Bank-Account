import java.util.ArrayList;
import java.util.List;

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
    }
}