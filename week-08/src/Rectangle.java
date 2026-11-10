/** A concrete subclass of Shape (slide p.12). */
public class Rectangle extends Shape {
    protected double w, h;

    /** Creates a 0 x 0 rectangle. */
    public Rectangle() {
        w = 0.0;
        h = 0.0;
    }

    /**
     * Creates a rectangle with the given width and height.
     *
     * @param w the width
     * @param h the height
     */
    public Rectangle(double w, double h) {
        this.w = w;
        this.h = h;
    }

    @Override
    public double area() {
        return w * h;
    }
}
