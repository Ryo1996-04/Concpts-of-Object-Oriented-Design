public class InstanceCalculator {
    // No static: call these methods through an object.
    public float sum(int num1, float num2) {
        return num1 + num2;
    }

    public float sum(float num1, int num2) {
        return num1 + num2;
    }

    public static void main(String[] args) {
        InstanceCalculator calculator = new InstanceCalculator();

        float result1 = calculator.sum(2, 3.5f);
        float result2 = calculator.sum(2.5f, 3);

        System.out.println(result1); // 5.5
        System.out.println(result2); // 5.5
    }
}
