import java.util.Scanner;

public class InputOutExample {

    public static void main(String[] args) {
        String fullName;
        int age;
        char sex;
        float salary;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter your full name");
        fullName = in.nextLine();
        System.out.println("Enter your age");
        age = in.nextInt();
        System.out.println("Enter the first letter of your sex");
        sex = in.next().charAt(0);
        System.out.println("Enter your salary");
        salary = in.nextFloat();
        System.out.printf("Your name is %s, your sex is %c, your age is %10d, and your salary is %.5f", fullName, sex, age, salary);
        
        
        

    }

}
