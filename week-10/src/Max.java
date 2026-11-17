// Class demo Max.java (Week 10, 2026-11-17), transcribed from the lecture.
// Only change: arr3 is Double[] instead of double[], so the file compiles
// (a double[] matches neither findMax(int[]) nor findMax(T[])).
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
        System.out.println(findMax(arr1));
        System.out.println(findMax(arr2));
        System.out.println(findMax(arr3));
    }
}
