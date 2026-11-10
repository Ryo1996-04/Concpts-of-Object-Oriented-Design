import java.util.Scanner;

public class DigitSum {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Math.abs(sc.nextInt());    // -984 → 984
        int sum = 0;
        while (n > 0) {
            sum += n % 10;                 // 取個位
            n /= 10;                       // 去個位
        }
        System.out.println(sum);           // 567→18, -984→21
    }
}
