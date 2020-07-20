package wtf.choco.evolve.event;

import java.util.function.Consumer;

/**
 * The point of access for event listening functionality.
 *
 * @author Parker Hawke
 */
public interface EventBus {

    /**
     * Evolve's event bus instance. All events called by Evolve will be pushed here.
     */
    public static final EventBus EVOLVE = new EvolveEventBus();

    /**
     * Push an {@link Event} to this event bus.
     *
     * @param event the event to push
     */
    public void push(Event event);

    /**
     * Subscribe to a specific event.
     *
     * @param event the event class to listen for
     * @param listener the function to be called when the event occurs
     * @param <T> the event to be listened for
     *
     * @return the subscribed listener instance
     */
    public <T extends Event> SubscribedListener<T> subscribeTo(Class<T> event, Consumer<T> listener);

    /**
     * Unregister a subscribed listener from this event bus.
     *
     * @param listener the listener to unregister
     */
    public void unregisterListener(SubscribedListener<?> listener);

    /**
     * Unregister all subscribed listeners from this event bus.
     */
    public void unregisterListeners();

}
