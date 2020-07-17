package wtf.choco.evolve.event.input;

import wtf.choco.evolve.event.Cancellable;
import wtf.choco.evolve.event.Event;

public class KeyPressEvent extends Event implements Cancellable {

    private boolean cancelled;
    private final int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

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
