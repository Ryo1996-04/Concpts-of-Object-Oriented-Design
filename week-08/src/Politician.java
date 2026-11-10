/** Implements Speaker (slide p.19). */
public class Politician implements Speaker {
    @Override
    public void speak() {           // must be public: interface methods are public
        System.out.println("Talk politics");
    }
}
