/** A concrete subclass of Shape (slide p.12). */
public class Circle extends Shape {
    protected double r;
    protected static final double PI = 3.1415;

    /** Creates a circle with radius 1.0. */
    public Circle() {
        r = 1.0;
    }

    /**
     * Creates a circle with the given radius.
     *
     * @param r the radius
     */
    public Circle(double r) {
        this.r = r;
    }

    @Override
    public double area() {
        return PI * r * r;
    }
}
