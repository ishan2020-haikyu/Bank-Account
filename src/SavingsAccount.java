public class SavingsAccount extends Account implements Transferable {
    private final double interestRate; // fraction: 0.05 = 5%

    public SavingsAccount(String owner, double balance, String accountNumber, double interestRate) {
        super(owner, balance, accountNumber);
        if (interestRate < 0)
            throw new IllegalArgumentException("Interest rate cannot be negative");
        this.interestRate = interestRate;
    }

    public void applyInterest() {
        double interest = getBalance() * interestRate;
        if (interest > 0) deposit(interest);
    }

    public double getInterestRate() { return interestRate; }

    @Override
    public void transfer(Account to, double amount) throws InsufficientFundsException {
        if (to == null)
            throw new IllegalArgumentException("Destination account is required");
        if (to == this)
            throw new IllegalArgumentException("Cannot transfer to the same account");
        withdraw(amount);
        to.deposit(amount);
    }

    @Override
    public String getAccountType() { return "Savings Account"; }
}