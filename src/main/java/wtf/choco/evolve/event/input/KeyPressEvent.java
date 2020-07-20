package wtf.choco.evolve.event.input;

import org.lwjgl.input.Keyboard;

import wtf.choco.evolve.event.Cancellable;
import wtf.choco.evolve.event.Event;

/**
 * Called when the client presses a key. If this event is cancelled, the key will not be
 * delegated to Equilinox and it will be ignored by any otherwise assigned Equilinox
 * functionality.
 *
 * @author Parker Hawke
 */
public class KeyPressEvent extends Event implements Cancellable {

    private boolean cancelled;
    private final int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

    /**
     * Get the key that was pressed in this event. For a list of more readable constants,
     * refer to {@link Keyboard}.
     *
     * @see Keyboard
     *
     * @return the pressed key
     */
    public int getKey() {
        return key;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

}
