public class Person {
    String name;            // Instance field: each object has its own name.
    int age;                // Instance field: each object has its own age.
    static int counter = 0; // Class variable: shared by all Person objects.

    public Person() {
        this("", 0); // Delegate initialization; this still creates only one object.
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        counter++; // Count each newly constructed Person once.
    }

    public static void main(String[] args) {
        Person p1 = new Person();
        Person p2 = new Person("Smith", 32);

        System.out.println(p1.counter);     // 2: legal, but class-name access is clearer.
        System.out.println(p2.counter);     // 2: the same shared field.
        System.out.println(Person.counter); // 2: recommended access syntax.
    }
}
