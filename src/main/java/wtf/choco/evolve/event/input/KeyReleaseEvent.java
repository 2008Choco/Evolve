package wtf.choco.evolve.event.input;

import wtf.choco.evolve.event.Event;

public class KeyReleaseEvent extends Event {

    private final int key;

    public KeyReleaseEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

}
