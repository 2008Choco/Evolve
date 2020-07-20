package wtf.choco.evolve.event;

import java.util.function.Consumer;

import com.google.common.base.Preconditions;

public final class SubscribedListener {

    private final EventBus bus;
    private final Class<? extends Event> eventClass;
    private final Consumer<Event> listener;

    public SubscribedListener(EventBus bus, Class<? extends Event> event, Consumer<Event> listener) {
        this.bus = bus;
        this.eventClass = event;
        this.listener = listener;
    }

    public EventBus getBus() {
        return bus;
    }

    public Class<? extends Event> getEventClass() {
        return eventClass;
    }

    public void call(Event event) {
        Preconditions.checkArgument(this.eventClass.isInstance(event), "Illegal event call. Expected " + this.eventClass.getName() + ", got " + event.getClass().getName());
        this.listener.accept(this.eventClass.cast(event));
    }

    public void unsubscribe() {
        this.bus.unregisterListener(this);
    }

}
