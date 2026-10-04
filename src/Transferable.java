public interface Transferable {

    void transfer(BankAccount to, double amount); // every implementor MUST provide this

    default String getTransferSummary() { // optional default implementation
        return "Transfer completed";
    }

}