import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Repository<T extends Identifiable> {
    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        if (item == null)
            throw new IllegalArgumentException("Item is required");
        if (findById(item.getId()).isPresent())
            throw new IllegalArgumentException("Duplicate id: " + item.getId());
        items.add(item);
    }

    public Optional<T> findById(int id) {
        for (T item : items) {
            if (item.getId() == id) return Optional.of(item);
        }
        return Optional.empty();
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(items);   // read-only view, protects internal list
    }
}