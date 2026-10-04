//// HashSet — fastest, no guaranteed order
//Set accountNumbers = new HashSet<>();
//accountNumbers.add("ACC001");
//accountNumbers.add("ACC002");
//accountNumbers.add("ACC001"); // duplicate — silently ignored
//System.out.println(accountNumbers.size()); // 2, not 3

//// Fast membership check — O(1)
//boolean exists = accountNumbers.contains("ACC001"); // true

//// LinkedHashSet — preserves insertion order
//Set ordered = new LinkedHashSet<>();

//// TreeSet — sorted alphabetically / naturally
//Set sorted = new TreeSet<>();
//sorted.add("ACC003"); sorted.add("ACC001"); sorted.add("ACC002");
//System.out.println(sorted); // [ACC001, ACC002, ACC003] — always sorted

//// IMPORTANT: For HashSet/HashMap to work correctly with custom objects,
//// you MUST override equals() and hashCode() in your class!
//@Override public boolean equals(Object o) { ... }
//@Override public int hashCode() { ... }