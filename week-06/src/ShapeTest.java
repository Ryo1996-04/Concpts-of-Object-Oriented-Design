public class ShapeTest {
    public static void main(String[] args) {
        Shape s1 = new Circle(2);
        Shape s2 = new Rectangle(3, 4);
        Shape s3 = new Shape();
        Shape s4 = new Circle(); // No-arg constructor: radius 1.

        // Every variable is declared as Shape; the object decides which area() runs.
        System.out.println(s1.area()); // 12.566370614359172: Circle's area()
        System.out.println(s2.area()); // 12.0: Rectangle's area()
        System.out.println(s3.area()); // 0.0: Shape's own area()
        System.out.println(s4.area()); // 3.141592653589793: Circle's area(), radius 1
    }
}
