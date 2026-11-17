/**
 * Week 10 p.2-3: printing arrays of different element types
 * with overloaded methods (one method per type).
 */
public class PrintArrayOverload {

    public static void printArray(int[] inputArray) {
        for (int element : inputArray) {
            System.out.printf("%s ", element);
        }
        System.out.println();
    }

    public static void printArray(double[] inputArray) {
        for (double element : inputArray) {
            System.out.printf("%s ", element);
        }
        System.out.println();
    }

    public static void printArray(long[] inputArray) {
        for (long element : inputArray) {
            System.out.printf("%s ", element);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] ints = {1, 2, 3};
        double[] doubles = {1.5, 2.5};
        long[] longs = {10000000000L, 20000000000L};

        printArray(ints);     // calls printArray(int[])
        printArray(doubles);  // calls printArray(double[])
        printArray(longs);    // calls printArray(long[])

        // float[] floats = {1.0f};
        // printArray(floats);  // compile error: no printArray(float[])
    }
}
