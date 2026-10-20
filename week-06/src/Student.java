public class Student extends Person { // is-a: a Student is a Person.
    protected int id;                 // Only the fields a Student adds.
    protected String major;

    public Student(String fName, int age, Address address, int id, String major) {
        super(fName, age, address); // Must be the first line; Person sets up its own part.
        this.id = id;
        this.major = major;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getMajor() { return this.major; }
    public void setMajor(String major) { this.major = major; }

    public static void main(String[] args) {
        Address addr = new Address("123", "Main St", 12345);
        Student s = new Student("Amy", 20, addr, 1001, "Software Engineering");

        System.out.println("Name: " + s.getFName());                     // Inherited from Person.
        System.out.println("Age: " + s.getAge());                        // Inherited from Person.
        System.out.println("Street: " + s.getAddress().getStreetName()); // Method chaining.
        System.out.println("ID: " + s.getId());                          // Defined in Student.
        System.out.println("Major: " + s.getMajor());                    // Defined in Student.
        System.out.println("Person count: " + Person.getCounter());      // 1: Person's constructor ran.
    }
}
