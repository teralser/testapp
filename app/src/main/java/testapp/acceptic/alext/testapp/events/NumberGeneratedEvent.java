package testapp.acceptic.alext.testapp.events;

/**
 * Created by Alex Tereshchenko on 09.11.2016.
 */

public class NumberGeneratedEvent {

    private final int number;

    public NumberGeneratedEvent(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
