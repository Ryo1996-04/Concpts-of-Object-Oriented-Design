/**
 * Week 10 p.4-8: one generic method replaces all the overloads.
 */
public class GenericPrint {

    /**
     * Prints every element of an array of any reference type.
     * <E> is the type parameter section; E is the type parameter.
     *
     * @param <E>        element type (must be a reference type)
     * @param inputArray the array to print
     */
    public static <E> void printArray(E[] inputArray) {
        for (E element : inputArray) {          // E as a local variable type
            System.out.printf("%s ", element);
        }
        System.out.println();
    }

    /**
     * Returns the first element; shows E used as the return type.
     *
     * @param <E>   element type
     * @param array a non-empty array
     * @return array[0]
     */
    public static <E> E first(E[] array) {
        E result = array[0];
        return result;
    }

    public static void main(String[] args) {
        Integer[] intArray = {1, 2, 3, 4, 5};             // autoboxing int -> Integer
        Double[] doubleArray = {1.1, 2.2, 3.3, 4.4};
        Character[] charArray = {'H', 'E', 'L', 'L', 'O'};
        String[] stringArray = {"Java", "Generics"};

        System.out.println("Array integerArray contains:");
        printArray(intArray);      // E = Integer
        System.out.println("\nArray doubleArray contains:");
        printArray(doubleArray);   // E = Double
        System.out.println("\nArray characterArray contains:");
        printArray(charArray);     // E = Character
        System.out.println("\nArray stringArray contains:");
        printArray(stringArray);   // E = String, no new method needed

        Integer x = first(intArray);   // no cast: compiler knows E = Integer
        String s = first(stringArray);
        System.out.println("\nfirst(intArray) + 1 = " + (x + 1));
        System.out.println("first(stringArray) = " + s);

        // Explicit type argument (rarely needed):
        GenericPrint.<Double>printArray(doubleArray);
    }
}
