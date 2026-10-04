// ABSTRACT CLASS — partial implementation. Can have fields + concrete methods

public abstract class Account {

    protected String owner;
    protected double balance;

    public Account(String owner, double balance) { // constructor allowed
        this.owner = owner;
        this.balance = balance;
    }

    public abstract String getAccountType(); // subclass MUST implement

    public double getBalance() { return balance; } // concrete method — shared

//     Combining both:
//    public class SavingsAccount extends Account implements Transferable {
//        public SavingsAccount(String owner, double balance) {
//            super(owner, balance);
//        }
//
//        @Override
//        public String getAccountType() {
//            return "Savings";
//        }
//
//        @Override
//        public void transfer(BankAccount to, double amount) {
//            this.balance -= amount;
//            to.deposit(amount);
//        }
//    }

}