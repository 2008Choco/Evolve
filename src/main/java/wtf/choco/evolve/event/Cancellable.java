package wtf.choco.evolve.event;

/**
 * Represents a stated {@link Event} which may be cancelled to prevent an action from occurring.
 *
 * @author Parker Hawke
 */
public interface Cancellable {

    /**
     * Set the cancellation state of this event.
     *
     * @param cancelled the new cancelled state
     */
    public void setCancelled(boolean cancelled);

    /**
     * Check whether or not this event has been cancelled.
     *
     * @return true if cancelled, false otherwise
     */
    public boolean isCancelled();

}
