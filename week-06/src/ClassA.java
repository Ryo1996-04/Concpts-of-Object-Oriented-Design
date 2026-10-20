// One public class per file: ClassA matches the file name, so main lives here.
public class ClassA {
    public ClassA() {
        System.out.println("A constructor");
    }

    public static void main(String[] args) {
        ClassC c = new ClassC(); // Prints A, B, C: calls go up the chain, bodies finish top-down.
    }
}

class ClassB extends ClassA {
    public ClassB() {
        // Java inserts super(); here because none is written.
        System.out.println("B constructor");
    }
}

class ClassC extends ClassB {
    public ClassC() {
        System.out.println("C constructor");
    }
}
