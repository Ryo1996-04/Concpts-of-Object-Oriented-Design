// Class demo Max.java (Week 10, 2026-11-17), final version shown in the lecture:
// arr3 changed to Double[] and arr4 (Circle[]) added.
// Circle here uses Comparable<Circle>; see Circle.java.
public class Max {
    public static int findMax(int [] data)
    {
        int max = data[0];

        for (int i = 1; i< data.length;i++)
        {
            if (data[i] > max)
            {
                max = data[i];
            }
        }
        return max;
    }
    public static <T extends Comparable<T>> T findMax(T[] data)
    {
        T max = data[0];
        for (int i = 1; i< data.length;i++)
        {
            if(max.compareTo(data[i])<0)
            {
                max = data[i];
            }
        }
        return max;
    }
    public static void main(String args[])
    {
        int[] arr1 = {-8,25,69,8,-56,25,4};
        String arr2[] = {"John", "Adams", "Barak", "Obama"};
        Double arr3[] = {25.5 ,89.9,0.25,69.88,-4.25};
        Circle[] arr4 = {new Circle(10), new Circle(59), new Circle(88), new Circle(150)};
        System.out.println(findMax(arr1));
        System.out.println(findMax(arr2));
        System.out.println(findMax(arr3));
        System.out.println(findMax(arr4).getRadius());
        System.out.println(findMax(arr4));   // uses Circle.toString()
    }
}
