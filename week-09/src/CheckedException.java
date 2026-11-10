import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/** The classroom FileReader example with its checked exceptions handled. */
public class CheckedException {

    /**
     * Prints a text file; the file name comes from args[0] or defaults to C:/example.txt.
     *
     * @param args optional file name
     */
    public static void main(String[] args) {
        String fileName = (args.length > 0) ? args[0] : "C:/example.txt";
        File input = new File(fileName);
        FileReader fr = null;
        try {
            fr = new FileReader(input);              // 打開：可能 FileNotFoundException
            int ch;
            while ((ch = fr.read()) != -1) {         // 讀取：可能 IOException
                System.out.print((char) ch);
            }
            System.out.println();
        } catch (FileNotFoundException e) {          // 子類別在前
            System.out.println("cannot open: " + e.getMessage());
        } catch (IOException e) {                    // 父類別在後
            System.out.println("read failed: " + e.getMessage());
        } finally {
            if (fr != null) {
                try {
                    fr.close();                      // close() 也可能 IOException
                } catch (IOException e) {
                    System.out.println("close failed: " + e.getMessage());
                }
            }
            System.out.println("finally: done");
        }
    }
}
