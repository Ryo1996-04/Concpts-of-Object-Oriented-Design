/** Uses the abstract type Shape as a reference type (slides p.10–13). */
public class ShapeTest {

    /**
     * Stores different shapes in one Shape[] and lets each one compute its own area.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // Shape s = new Shape();   // compile error: Shape is abstract; cannot be instantiated
        Shape[] shapes = { new Circle(), new Circle(2.0), new Rectangle(3.0, 4.0) };
        for (Shape s : shapes) {
            s.describe();           // describe() calls the subclass's own area()
        }
    }
}
