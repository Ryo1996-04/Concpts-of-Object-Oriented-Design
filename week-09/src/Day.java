/** The seven days of the week, declared in calendar order. */
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    /**
     * Prints every constant with values(), then classifies two days with switch.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        for (Day d : Day.values()) {
            System.out.println(d.ordinal() + " " + d);
        }
        describe(Day.WEDNESDAY);
        describe(Day.SUNDAY);
    }

    /**
     * Prints whether the given day is a weekday or the weekend.
     *
     * @param day the day to classify
     */
    static void describe(Day day) {
        switch (day) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                System.out.println(day + " is a weekday");
                break;
            case SATURDAY:
            case SUNDAY:
                System.out.println(day + " is the weekend");
                break;
        }
    }
}
