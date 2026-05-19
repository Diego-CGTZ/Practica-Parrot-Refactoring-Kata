package parrot;

public abstract class Parrot {

    protected Parrot() {
    }

    public abstract double getSpeed();



    protected double getBaseSpeed() {
        return 12.0;
    }

    public abstract String getCry();
}
