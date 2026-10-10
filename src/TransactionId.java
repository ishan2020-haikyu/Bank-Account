import java.util.Objects;

public final class TransactionId {
    private final String value;

    public TransactionId(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Transaction ID is required");
        this.value = value.trim().toUpperCase();   // normalise so "txn1001" equals "TXN1001"
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;                       // same reference
        if (o == null || getClass() != o.getClass())      // null or different type
            return false;
        TransactionId other = (TransactionId) o;
        return value.equals(other.value);                 // compare the meaningful field
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);                       // uses the SAME field as equals()
    }

    @Override
    public String toString() {
        return value;
    }
}


