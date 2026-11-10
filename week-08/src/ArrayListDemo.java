import java.util.ArrayList;

/** Slide p.7 with a generic ArrayList<String>; prints size and contents after each step. */
public class ArrayListDemo {

    /**
     * Adds, inserts and removes elements, then shows get, contains, indexOf and set.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        ArrayList<String> al = new ArrayList<>();          // empty list
        System.out.println("Initial size of al: " + al.size());

        al.add("C");
        al.add("A");
        al.add("E");
        al.add("B");
        al.add("D");
        al.add("F");
        al.add(1, "A2");                                    // insert at index 1
        System.out.println("Size of al after additions: " + al.size());
        System.out.println("Contents of al: " + al);

        al.remove("F");                                     // remove(Object): by value
        al.remove(2);                                       // remove(int): by index
        System.out.println("Size of al after deletions: " + al.size());
        System.out.println("Contents of al: " + al);

        System.out.println("get(0): " + al.get(0));
        System.out.println("contains(\"B\"): " + al.contains("B"));
        System.out.println("indexOf(\"Z\"): " + al.indexOf("Z"));
        al.set(0, "Z");                                     // replace index 0
        System.out.println("after set(0, \"Z\"): " + al);

        for (String s : al) {                               // enhanced for, like an array
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
