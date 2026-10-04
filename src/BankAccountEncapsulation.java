public class BankAccountEncapsulation {
    private double balance; // PRIVATE — outsiders cannot touch this directly
    // Controlled access — we validate before changing state
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.balance += amount;
    }
    public void withdraw(double amount) {
        if (amount > balance) throw new IllegalStateException("Insufficient funds");
        this.balance -= amount;
    }
    public double getBalance() { return balance; } // read-only getter
// No setBalance() — we never let anyone set balance directly!
}
// This compiles: account.deposit(500);
// This does NOT: account.balance = -99999; // compile error!