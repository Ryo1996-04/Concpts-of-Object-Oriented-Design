/**
 * A traffic light described with three enums: power state, color and direction.
 * Each enum is a fixed set of values that is known at compile time.
 */
public class TrafficLight {

    /** The light is either switched on or off. */
    enum Power { ON, OFF }

    /** Each color stores how many seconds it stays lit. */
    enum LightColor {
        RED(30), YELLOW(5), GREEN(25);

        private final int seconds;

        /** Enum constructors are private; Java calls them once per constant. */
        LightColor(int seconds) {
            this.seconds = seconds;
        }

        /** Returns how many seconds this color stays lit. */
        int getSeconds() {
            return seconds;
        }

        /** Returns the color that comes after this one: GREEN, YELLOW, RED, GREEN, ... */
        LightColor next() {
            switch (this) {
                case GREEN:
                    return YELLOW;
                case YELLOW:
                    return RED;
                default:
                    return GREEN;
            }
        }
    }

    /** The traffic flow this light controls. */
    enum Direction { NORTH_SOUTH, EAST_WEST }

    private Power power = Power.OFF;
    private LightColor color = LightColor.RED;
    private final Direction direction;

    /** Creates a light that is off, shows RED and controls the given direction. */
    public TrafficLight(Direction direction) {
        this.direction = direction;
    }

    /** Switches the light on. */
    public void turnOn() {
        power = Power.ON;
    }

    /** Moves to the next color; does nothing while the power is off. */
    public void change() {
        if (power == Power.ON) {
            color = color.next();
        }
    }

    @Override
    public String toString() {
        return direction + " " + power + " " + color + " (" + color.getSeconds() + "s)";
    }

    /**
     * Runs one full color cycle and shows the built-in enum methods.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        TrafficLight light = new TrafficLight(Direction.NORTH_SOUTH);
        System.out.println(light);
        light.change();
        System.out.println(light);

        light.turnOn();
        for (int i = 0; i < 3; i++) {
            light.change();
            System.out.println(light);
        }

        LightColor c = LightColor.valueOf("YELLOW");
        System.out.println(c.name() + " ordinal=" + c.ordinal());
        System.out.println(c == LightColor.YELLOW);
        System.out.println(LightColor.values().length);
    }
}
