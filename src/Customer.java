public class Customer implements Identifiable {
    private final int id;
    private final String name;

    public Customer(int id, String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name is required");
        this.id = id;
        this.name = name;
    }

    @Override public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() { return "Customer{id=" + id + ", name=" + name + "}"; }
}