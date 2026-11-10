import java.util.Scanner;              // Scanner 在 java.util，要 import

public class ReadName {
    public static void main(String[] args) {     // 程式入口
        Scanner sc = new Scanner(System.in);     // System.in = 鍵盤輸入
        System.out.print("Name: ");
        String name = sc.nextLine();             // 程式停在這裡，等使用者輸入
        System.out.println("Hello, " + name);
        sc.close();
    }
}
