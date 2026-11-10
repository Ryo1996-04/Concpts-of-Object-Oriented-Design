/** Polymorphism via interfaces (slides p.23–24). */
public class SpeakerTest {

    /**
     * Calls speak() through Speaker and Teacher references.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // p.24: one reference, different objects, different speak()
        Speaker guest = new Lecturer();
        guest.speak();
        guest = new Politician();
        guest.speak();

        // p.23: one interface type for an array of unrelated classes
        Speaker[] speakers = { new Politician(), new Priest(), new Lecturer(), new Professor("Dr. Lee") };
        for (Speaker current : speakers) {
            current.speak();
        }

        // p.20–21: Professor is a Person, a Teacher and a Speaker
        Professor p = new Professor("Dr. Lee");
        Teacher t = p;
        t.grade("Claire");
        System.out.println(p.getName() + " is a Speaker: " + (p instanceof Speaker));

        System.out.println("Speaker.MAX_MINUTES = " + Speaker.MAX_MINUTES);
        // Speaker.MAX_MINUTES = 90;   // compile error: cannot assign a value to static final variable MAX_MINUTES
    }
}
