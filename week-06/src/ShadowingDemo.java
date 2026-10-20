public class ShadowingDemo {
    public static void main(String[] args) {
        new Derived().show();
    }
}

class Base {
    int x = 1;
}

class Derived extends Base {
    int x = 2; // Shadows Base's x; avoid this in real code.

    void show() {
        System.out.println(this.x);  // 2: Derived's x
        System.out.println(super.x); // 1: Base's x
    }
}
