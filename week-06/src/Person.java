public class Person {
    protected String fName;         // protected: subclasses can use these fields directly.
    protected int age;
    protected Address address;      // has-a: a Person owns an Address.
    private static int counter = 0; // Class variable: counts every Person, including subclasses.

    public Person(String fName, int age, Address address) {
        this.fName = fName;
        this.age = age;
        this.address = address;
        counter++; // Also runs when a Student is created, through super(...).
    }

    public String getFName() { return this.fName; }
    public void setFName(String fName) { this.fName = fName; }

    public int getAge() { return this.age; }
    public void setAge(int age) { this.age = age; }

    public Address getAddress() { return this.address; }
    public void setAddress(Address address) { this.address = address; }

    public static int getCounter() { return counter; }
}
