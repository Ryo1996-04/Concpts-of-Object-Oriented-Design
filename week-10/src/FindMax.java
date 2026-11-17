/**
 * Week 10 whiteboard exercise: "Write a method that accepts an array of ints
 * and return the maximum value", then generalize it step by step (p.10-12).
 */
public class FindMax {

    /** Step 1: int only, compared with >. */
    public static int maximum(int[] array) {
        int max = array[0];                  // assume the first one is the largest
        for (int element : array) {
            if (element > max) {
                max = element;
            }
        }
        return max;
    }

    /** Step 2: double needs another overload with the same logic. */
    public static double maximum(double[] array) {
        double max = array[0];
        for (double element : array) {
            if (element > max) {
                max = element;
            }
        }
        return max;
    }

    /** Step 3: wrapper Integer, compared with compareTo() instead of >. */
    public static Integer maximumInteger(Integer[] array) {
        Integer max = array[0];
        for (Integer element : array) {
            if (element.compareTo(max) > 0) {
                max = element;
            }
        }
        return max;
    }

    /**
     * Step 4: generic. Comparable is the upper bound of T,
     * so every T is guaranteed to have compareTo().
     *
     * @param <T>   any type that can be compared with itself
     * @param array a non-empty array
     * @return the largest element
     */
    public static <T extends Comparable<T>> T maximumGeneric(T[] array) {
        T max = array[0];
        for (T element : array) {
            if (element.compareTo(max) > 0) {
                max = element;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println("Step 1 int[]:      " + maximum(new int[] {3, 9, 2, 7}));
        System.out.println("Step 2 double[]:   " + maximum(new double[] {1.5, -2.0, 8.25}));
        System.out.println("Step 3 Integer[]:  " + maximumInteger(new Integer[] {3, 9, 2, 7}));
        System.out.println("Step 4 Integer[]:  " + maximumGeneric(new Integer[] {3, 9, 2, 7}));
        System.out.println("Step 4 Double[]:   " + maximumGeneric(new Double[] {1.5, -2.0, 8.25}));
        System.out.println("Step 4 Character[]: " + maximumGeneric(new Character[] {'J', 'a', 'v', 'A'}));
        System.out.println("Step 4 String[]:   " + maximumGeneric(new String[] {"pear", "apple", "zebra", "mango"}));
    }
}
