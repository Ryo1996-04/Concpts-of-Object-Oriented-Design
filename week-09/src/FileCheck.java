import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/** Handles the checked exceptions from slide p.9 so the program compiles. */
public class FileCheck {

    /**
     * Tries to open logs.txt in the current directory.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        File input = new File("logs.txt");
        try {
            FileReader fr = new FileReader(input);   // may throw FileNotFoundException
            System.out.println("file opened");
            fr.close();                              // may throw IOException
        } catch (FileNotFoundException e) {          // subclass first
            System.out.println("not found: " + e.getMessage());
        } catch (IOException e) {                    // superclass after
            System.out.println("I/O problem: " + e.getMessage());
        }
    }
}
