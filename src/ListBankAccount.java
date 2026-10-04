//import java.util.*;
//// ArrayList — backed by a resizable array. Fast random access. Slow insert/delete at middle.
//List accounts = new ArrayList<>();
//accounts.add(new BankAccount("Raj", 10000));
//        accounts.add(new BankAccount("Priya", 2500));
//        accounts.add(new BankAccount("Arjun", 7500));
//        // Access by index
//        BankAccount first = accounts.get(0); // Raj's account
//        int size = accounts.size(); // 3
//// Iterate — most common pattern
//for (BankAccount acc : accounts) {
//        System.out.println(acc.getOwner() + ": " + acc.getBalance());
//        }
//// Remove
//        accounts.remove(0); // remove by index
//accounts.remove(priyaAccount); // remove by object (uses .equals())
//        // Check membership
//        boolean exists = accounts.contains(rajAccount);
//// Sort by balance (uses Comparator — preview of Week 5 lambdas)
//accounts.sort(Comparator.comparingDouble(BankAccount::getBalance));