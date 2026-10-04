public class BankAccount {

    // Field — lives on the HEAP inside the object
    private double balance;
    private final String owner;
    private final String accountNumber;

    // Constructor — called when you write: new BankAccount(1000)
    public BankAccount(String owner, double initialBalance, String accountNumber) {
        if (owner == null || owner.isBlank())
            throw new IllegalArgumentException("Owner is required");
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Account number is required");
        if (initialBalance < 0)
            throw new IllegalArgumentException("Initial balance cannot be negative");
        this.owner = owner;
        this.balance = initialBalance;
        this.accountNumber = accountNumber;
    }

    // Method — gets its own STACK frame when called
    public void deposit(double amount) {
        // 'amount' is a local variable — lives on the stack
        // 'this.balance' is a field — lives on the heap
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.balance += amount;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getOwner() {
        return this.owner;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                    "Cannot withdraw " + amount + ". Available balance: " + balance);
        }
        balance -= amount;
    }

    public String getAccountType() {
        return "Standard Account";
    }

    public static void tryToReplace(BankAccount acc){
        acc = new BankAccount("Kamlesh", 0,"HDFC000044115522");
    }


    public static void main(String[] args) {

        // 'account' is a reference — on the stack
        // The BankAccount OBJECT itself is on the HEAP
        BankAccount account01 = new BankAccount("Raj",1000.0, "SBI00001478963");
        account01.deposit(500.0);
        System.out.println("Current Balance - " + account01.getBalance()); // prints 1500.0
        account01.deposit(250.0);
        System.out.println("Current Balance - " + account01.getBalance()); // prints 1750.0
        account01.deposit(2500.0);
        System.out.println("Current Balance - " + account01.getBalance()); // prints 4250.0
        tryToReplace(account01);
        System.out.println("Current Balance - " + account01.getBalance()); // prints 4250.0

        //Transfer System
        BankAccount account02 = new BankAccount("Raju", 2000.0, "HDFC00112233");
        transfer(account02, account01, 500.0);

    }

    private static void transfer(BankAccount account02, BankAccount account01, double v) {
        if(v <= 0){
            System.out.println("Invalid Transfer");
        }
        account02.balance -= v;
        account01.balance += v;
        System.out.println("Transfer Successful");
    }

}

