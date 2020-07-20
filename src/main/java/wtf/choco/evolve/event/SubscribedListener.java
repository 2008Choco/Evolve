package wtf.choco.evolve.event;

import java.util.function.Consumer;

import com.google.common.base.Preconditions;

public final class SubscribedListener<T extends Event> {

    private final EventBus bus;
    private final Class<T> eventClass;
    private final Consumer<T> listener;

    public SubscribedListener(EventBus bus, Class<T> event, Consumer<T> listener) {
        this.bus = bus;
        this.eventClass = event;
        this.listener = listener;
    }

    public EventBus getBus() {
        return bus;
    }

    public Class<T> getEventClass() {
        return eventClass;
    }

    public void call(Event event) {
        Preconditions.checkArgument(eventClass.isInstance(event), "Illegal event call. Expected " + eventClass.getName() + ", got " + event.getClass().getName());
        this.listener.accept(eventClass.cast(event));
    }

    public void unsubscribe() {
        this.bus.unregisterListener(this);
    }

}
