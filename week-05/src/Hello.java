public class Hello {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please enter first name and last name.");
            return;
        }

        System.out.println("Hello " + args[0] + " " + args[1]);
    }
}
