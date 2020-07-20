package wtf.choco.evolve.event.input;

import org.lwjgl.input.Keyboard;

import wtf.choco.evolve.event.Event;

/**
 * Called when the client releases a key.
 *
 * @author Parker Hawke
 */
public class KeyReleaseEvent extends Event {

    private final int key;

    public KeyReleaseEvent(int key) {
        this.key = key;
    }

    /**
     * Get the key that was released in this event. For a list of more readable constants,
     * refer to {@link Keyboard}.
     *
     * @see Keyboard
     *
     * @return the released key
     */
    public int getKey() {
        return key;
    }

}
