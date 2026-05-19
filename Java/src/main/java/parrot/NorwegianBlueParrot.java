package parrot;

public class NorwegianBlueParrot extends Parrot {
    private final double voltage;
    private final boolean isNailed;

    public NorwegianBlueParrot(double voltage, boolean isNailed) {
        super();
        this.voltage = voltage;
        this.isNailed = isNailed;
    }

    private static final double MAXIMUM_SPEED = 24.0;

    @Override
    public double getSpeed() {
        return isNailed ? 0 : Math.min(MAXIMUM_SPEED, voltage * getBaseSpeed());
    }

    @Override
    public String getCry() {
        return voltage > 0 ? "Bzzzzzz" : "...";
    }
}
