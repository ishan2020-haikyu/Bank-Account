import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionLogger implements AutoCloseable {
    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final FileWriter writer;
    private boolean closed = false;

    public TransactionLogger(String fileName) throws IOException {
        if (fileName == null || fileName.isBlank())
            throw new IllegalArgumentException("File name is required");
        this.writer = new FileWriter(fileName, true);   // true = append mode
        System.out.println("[logger] opened " + fileName);
    }

    public void log(String message) throws IOException {
        if (closed)
            throw new IllegalStateException("Logger is closed");
        writer.write("[" + LocalDateTime.now().format(FORMAT) + "] " + message + System.lineSeparator());
        writer.flush();   // push to disk now, so a crash doesn't lose the entry
    }

    @Override
    public void close() throws IOException {
        if (closed) return;          // safe to call twice
        closed = true;
        System.out.println("[logger] closed");
        writer.close();              // flushes remaining data, then releases the file
    }
}