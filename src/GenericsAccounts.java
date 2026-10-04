//public class GenericsAccounts {
//}


//// WITHOUT generics (old Java — error-prone)
//List accounts = new ArrayList(); // raw type
//accounts.add("oops a String"); // compiles — wrong type added silently
//BankAccount b = (BankAccount) accounts.get(0); // ClassCastException at RUNTIME

//// WITH generics (modern Java — safe)
//List accounts = new ArrayList<>();
//accounts.add("oops a String"); // COMPILE ERROR — caught immediately
//BankAccount b = accounts.get(0); // no cast needed — type is guaranteed

// Writing your own generic class
//public class Pair { // A and B are type parameters
//    private A first;
//    private B second;
//    public Pair(A first, B second) {
//        this.first = first;
//        this.second = second;
//    }
//    public A getFirst() { return first; }
//    public B getSecond() { return second; }
//}
