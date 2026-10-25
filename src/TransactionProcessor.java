import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TransactionProcessor {
    private final Bank bank;
    private int processed = 0;
    private int failed = 0;

    public TransactionProcessor(Bank bank) {
        if (bank == null) throw new IllegalArgumentException("Bank is required");
        this.bank = bank;
    }

    public void processFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (lineNo == 1 || line.isBlank()) continue;   // skip header and blank lines
                processLine(lineNo, line);
            }
        } catch (FileNotFoundException e) {                    // must come BEFORE IOException
            System.err.println("[FILE] Not found: " + path);
        } catch (IOException e) {
            System.err.println("[IO] Failed while reading " + path + ": " + e.getMessage());
        }
    }

    private void processLine(int lineNo, String line) {
        try {
            String[] parts = line.split(",", -1);              // -1 keeps trailing empty columns
            if (parts.length != 3)
                throw new IllegalArgumentException("Expected 3 columns but found " + parts.length);

            String accountId = parts[0].trim();
            TransactionType type = parseType(parts[1].trim());
            double amount = Double.parseDouble(parts[2].trim());   // may throw NumberFormatException

            Account account = bank.findAccount(accountId);         // may throw AccountNotFoundException

            switch (type) {
                case CREDIT -> account.deposit(amount);
                case DEBIT  -> account.withdraw(amount);           // may throw InsufficientFundsException
                default     -> throw new IllegalArgumentException("Unsupported type: " + type);
            }
            processed++;

        } catch (NumberFormatException e) {                    // child of IllegalArgumentException: first
            logError(lineNo, "BAD NUMBER", "Amount is not a number", line);
        } catch (InvalidAmountException e) {                   // also a child of IllegalArgumentException
            logError(lineNo, "INVALID AMOUNT", e.getMessage(), line);
        } catch (IllegalArgumentException e) {                 // wrong column count, unknown type
            logError(lineNo, "MALFORMED", e.getMessage(), line);
        } catch (AccountNotFoundException e) {
            logError(lineNo, "UNKNOWN ACCOUNT", e.getMessage(), line);
        } catch (InsufficientFundsException e) {
            logError(lineNo, "INSUFFICIENT FUNDS", e.getMessage(), line);
        }
    }

    private TransactionType parseType(String text) {
        try {
            return TransactionType.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown transaction type: '" + text + "'");
        }
    }

    private void logError(int lineNo, String category, String detail, String rawLine) {
        failed++;
        System.err.println("[LINE " + lineNo + "] " + category + ": " + detail + "  | " + rawLine);
    }

    public int getProcessed() { return processed; }
    public int getFailed() { return failed; }
}