public class SavingsAccount extends BankAccount{

    private double interestRate; // NEW field only in SavingsAccount

    public SavingsAccount(String owner, double balance, double rate) {
        super(owner, balance); // calls BankAccount constructor
        this.interestRate = rate;
    }

    public SavingsAccount() {
        System.out.println("Savings Account Created");
    }

    // NEW behaviour only in SavingsAccount
    public void applyInterest() {
        double interest = this.getBalance() * interestRate;
        deposit(interest); // reuses parent's deposit method
    }

    @Override // tells compiler we're intentionally overriding
    public String getAccountType() {
        return "Savings Account"; // different behaviour, same method name
    }

    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount("Raj", 10000, 0.05);
        System.out.println("Current Balance before interest - " + savings.getBalance());
        savings.applyInterest(); // balance becomes 10500.0
        System.out.println("Current Balance after interest - " + savings.getBalance());
    }
}


