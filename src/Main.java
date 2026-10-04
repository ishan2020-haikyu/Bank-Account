public class Main {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("Raj", 1000.0, "SBI00001478963");
        account.deposit(500);
        System.out.println(account.getBalance());

        try {
            account.withdraw(5000);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}