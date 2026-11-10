/** throws, a user-defined exception, and rethrowing (slides p.26, p.28–30). */
public class AgeDemo {
    private int age;

    /**
     * Sets the age.
     *
     * @param age the new age
     * @throws InvalidAgeException if age is negative
     */
    public void setAge(int age) throws InvalidAgeException {
        if (age < 0) {
            throw new InvalidAgeException("age cannot be negative: " + age);
        }
        this.age = age;
    }

    /**
     * Logs a failed update, then rethrows the same exception to the caller.
     *
     * @param p the object to update
     * @param age the new age
     * @throws InvalidAgeException if age is negative
     */
    static void update(AgeDemo p, int age) throws InvalidAgeException {
        try {
            p.setAge(age);
        } catch (InvalidAgeException e) {
            System.out.println("update(): logging -> " + e.getMessage());
            throw e;                                   // rethrow
        }
    }

    /**
     * Tries one valid and one invalid age.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        AgeDemo p = new AgeDemo();
        int[] inputs = {27, -5};
        for (int a : inputs) {
            try {
                update(p, a);
                System.out.println("age set to " + p.age);
            } catch (InvalidAgeException e) {
                System.out.println("main(): caught -> " + e.getMessage());
            }
        }
    }
}
