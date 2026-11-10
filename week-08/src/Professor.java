/** Extends one class and implements an interface at the same time (slide p.21). */
public class Professor extends Person implements Teacher {

    /**
     * Creates a professor.
     *
     * @param name the professor's name
     */
    public Professor(String name) {
        super(name);
    }

    @Override
    public void speak() {                       // from Speaker (via Teacher)
        System.out.println(name + ": Today we cover interfaces.");
    }

    @Override
    public void grade(String student) {         // from Teacher
        System.out.println(name + " grades " + student);
    }
}
