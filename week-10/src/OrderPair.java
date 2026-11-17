/**
 * A generic class with two type parameters: K (key) and V (value).
 * The type parameters are in scope for the whole class.
 */
public class OrderPair<K, V> {
    private K key;                       // K as a field type
    private V value;                     // V as a field type

    public OrderPair(K key, V value) {   // K, V as parameter types
        this.key = key;
        this.value = value;
    }

    public K getKey() {                  // K as a return type
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + " = " + value;
    }

    public static void main(String[] args) {
        OrderPair<String, Integer> p1 = new OrderPair<>("age", 25);       // K=String, V=Integer
        OrderPair<String, String> p2 = new OrderPair<>("name", "Ada"); // K=String, V=String

        String k = p1.getKey();    // no cast needed
        int v = p1.getValue();     // Integer is unboxed to int
        System.out.println(k + " -> " + (v + 1));
        System.out.println(p1);
        System.out.println(p2);

        // OrderPair<String, Integer> p3 = new OrderPair<>("age", "25"); // compile error: V is Integer
    }
}
