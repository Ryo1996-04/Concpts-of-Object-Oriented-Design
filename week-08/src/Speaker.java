/** An interface: every implementing class must supply speak() (slides p.16–17). */
public interface Speaker {
    int MAX_MINUTES = 60;           // implicitly public static final (a constant)

    /** Says something; implicitly public abstract. */
    void speak();
}
