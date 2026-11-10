import java.util.InputMismatchException;
import java.util.Scanner;

/** try / throw / multiple catch / finally around integer division (slides p.11–27). */
public class DivideDemo {

    /**
     * Divides a by b.
     *
     * @param a the dividend
     * @param b the divisor
     * @return a / b using integer division
     * @throws ArithmeticException if b is 0
     */
    public static int div(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("second number must not be 0");
        }
        return a / b;
    }

    /**
     * Reads two integers and prints their quotient.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        try {
            System.out.println("enter the first number");
            int num1 = input.nextInt();
            System.out.println("enter the second number");
            int num2 = input.nextInt();
            int result = div(num1, num2);
            System.out.println("result = " + result);   // skipped when div throws
        } catch (ArithmeticException e) {
            System.out.println("ArithmeticException: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("InputMismatchException: please enter integers");
        } finally {
            input.close();
            System.out.println("finally: scanner closed");
        }
        System.out.println("program continues");
    }
}
