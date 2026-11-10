/** A sub-interface: inherits speak() from Speaker and adds grade() (slide p.20). */
public interface Teacher extends Speaker {

    /**
     * Grades one student.
     *
     * @param student the student's name
     */
    void grade(String student);
}
