/** A plain superclass used to show extends + implements together (slide p.21). */
public class Person {
    protected String name;

    /**
     * Creates a person.
     *
     * @param name the person's name
     */
    public Person(String name) {
        this.name = name;
    }

    /**
     * Returns the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
