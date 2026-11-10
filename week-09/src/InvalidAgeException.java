/** A user-defined checked exception for a negative age (slides p.5, p.28–30). */
public class InvalidAgeException extends Exception {
    private static final long serialVersionUID = 1L;   // silences the Eclipse / -Xlint "serial" warning

    /**
     * Creates the exception with a message that getMessage() returns.
     *
     * @param message what went wrong
     */
    public InvalidAgeException(String message) {
        super(message);
    }
}
