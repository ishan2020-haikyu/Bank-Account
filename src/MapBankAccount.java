//// HashMap — most common. Keys must be unique. No guaranteed order.
//Map accountMap = new HashMap<>();

//// Put — add or update a key-value pair
//accountMap.put("ACC001", new BankAccount("Raj", 10000));
//        accountMap.put("ACC002", new BankAccount("Priya", 2500));
//        accountMap.put("ACC003", new BankAccount("Arjun", 7500));

//// Get — O(1) lookup by key
//BankAccount raj = accountMap.get("ACC001"); // instant, no looping

//// Safe get — returns default if key doesn't exist
//BankAccount acc = accountMap.getOrDefault("ACC999", null);

//// Check key existence
//boolean exists = accountMap.containsKey("ACC001"); // true

//// Iterate over all entries
//for (Map.Entry entry : accountMap.entrySet()) {
//        System.out.println(entry.getKey() + " -> " + entry.getValue().getBalance());
//        }

//// putIfAbsent — only insert if key not already present
//        accountMap.putIfAbsent("ACC001", new BankAccount("Other", 0)); // ignored

//// Remove
//        accountMap.remove("ACC002");


