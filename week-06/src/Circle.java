public class Circle extends Shape {
    private double radius;

    public Circle() {
        this(1); // Overloading: same class, different parameters.
    }

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() { // Overriding: same signature as Shape.area(), new body.
        return Math.PI * radius * radius;
    }
}
