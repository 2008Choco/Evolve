package wtf.choco.evolve.event;

import java.util.function.Consumer;

import com.google.common.base.Preconditions;

/**
 * Represents an event listener that has been subscribed to an {@link EventBus}.
 *
 * @param <T> the event to which this listener is listening
 *
 * @author Parker Hawke
 */
public final class SubscribedListener<T extends Event> {

    private final EventBus bus;
    private final Class<T> eventClass;
    private final Consumer<T> listener;

    SubscribedListener(EventBus bus, Class<T> event, Consumer<T> listener) {
        Preconditions.checkArgument(bus != null, "bus cannot be null");
        Preconditions.checkArgument(event != null, "event cannot be null");
        Preconditions.checkArgument(listener != null, "listener cannot be null");

        this.bus = bus;
        this.eventClass = event;
        this.listener = listener;
    }

    /**
     * Get the {@link EventBus} to which this listener has been registered.
     *
     * @return the event bus
     */
    public EventBus getBus() {
        return bus;
    }

    /**
     * Get the event class this listener is listening to.
     *
     * @return the event class
     */
    public Class<T> getEventClass() {
        return eventClass;
    }

    /**
     * Call this listener with the given event.
     *
     * @param event the event to call
     *
     * @throws IllegalArgumentException if the event called is of an unexpected type
     */
    public void call(Event event) {
        Preconditions.checkArgument(eventClass.isInstance(event), "Illegal event call. Expected " + eventClass.getName() + ", got " + event.getClass().getName());
        this.listener.accept(eventClass.cast(event));
    }

    /**
     * Unsubscribe this event listener from its event bus. This is a convenience method and
     * is equivalent to {@code getBus().unregisterListener(this)}.
     */
    public void unsubscribe() {
        this.bus.unregisterListener(this);
    }

}
