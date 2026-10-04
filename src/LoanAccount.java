import java.util.List;

public class LoanAccount extends BankAccount {

    private double creditLimit;

    public LoanAccount(String owner, double initialBalance) {
        super(owner, initialBalance);
    }

//    public LoanAccount() {
//        System.out.println("Loan Account Created");
//    }



    @Override
    public String getAccountType() { return "Loan Account"; }

    public static void main(String[] args) {
        // POLYMORPHISM in action:
//        BankAccount a = new BankAccount();
        BankAccount b = new SavingsAccount(); // parent type, child object
//        BankAccount c = new LoanAccount();
        // Same method call — three different results at runtime:
        System.out.println(a.getAccountType()); // Standard Account
        System.out.println(b.getAccountType()); // Savings Account
        System.out.println(c.getAccountType()); // Loan Account
        // This is the power — process a list of any account type uniformly:
//        List accounts = List.of(a, b, c);
//        accounts.forEach(acc -> System.out.println(acc.getAccountType()));
    }
}