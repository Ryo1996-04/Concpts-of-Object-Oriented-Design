/** An abstract class: it cannot be instantiated, and every concrete subclass must implement area(). */
public abstract class Shape {

    /**
     * Returns the area of this shape.
     *
     * @return the area
     */
    public abstract double area();          // abstract method: no body

    /** A concrete (non-abstract) method that every subclass inherits as is. */
    public void describe() {
        System.out.println(getClass().getSimpleName() + " area = " + area());
    }
}
