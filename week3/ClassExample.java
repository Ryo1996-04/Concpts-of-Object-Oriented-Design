import java.util.Scanner;

public class ClassExample {

    public static void main(String[] args) {
        
        float grade[] = new float[5];
        float  avg=0.0f, sum =0.0f; 
        Scanner in = new Scanner (System.in);
       for(int i = 0; i<grade.length;i++)
          {
           System.out.println("Enter the grade");
                grade[i] =  in.nextFloat();
                sum+=grade[i];
          }
       
              avg = sum/5;
             
              for(int i = 0; i<grade.length;i++)
              {
                  if(grade[i] < avg)
                     System.out.println(grade[i]);
                  }
    }

}
