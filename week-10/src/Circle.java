/**
 * Circle from the Week 10 class demo, ordered by radius.
 * The lecture version used the raw type "implements Comparable" with
 * compareTo(Object obj) and a cast; this version uses Comparable<Circle>,
 * so no cast is needed and comparing with a non-Circle is a compile error.
 */
public class Circle extends Shape implements Comparable<Circle> {

    public static final float PI = 3.14f;
    private float radius;

    public Circle(float radius) {
        super();
        this.radius = radius;
    }

    @Override
    public int compareTo(Circle other) {
        if (this.radius > other.radius) {
            return 1;
        } else if (this.radius < other.radius) {
            return -1;
        } else {
            return 0;
        }
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "" + this.getRadius();
    }
}
