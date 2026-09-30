
public class StringExample {

    public static void main(String[] args) {
               String fName = new String("John");
               String fName2 = new String("John");
               String lName = "Smith";
               String mName = new String(new char[] {'K','i','.',' '});
               String fullName = fName + " "+ mName+  lName;
               
               System.out.println("The full name is: "+ fullName);
                System.out.println(fullName.length());
                System.out.println(fullName.charAt(5));  // k
                System.out.println(fullName.indexOf("i"));  //6
                System.out.println(fullName.lastIndexOf("i"));  // 11
                System.out.println(fullName.substring(0,4));//John
                System.out.println(fullName.substring(9));  // Smith
                
                System.out.println( (fName == fName2)); // false
                System.out.println( fName.equals(fName2)); // true

    }

}
