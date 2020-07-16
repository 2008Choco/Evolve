package wtf.choco.evolve.event;

import java.util.function.Consumer;

public interface EventBus {

    public static final EventBus EVOLVE = new EvolveEventBus();

    public void push(Event event);

    public SubscribedListener subscribeTo(Class<? extends Event> event, Consumer<Event> listener);

    public void unregisterListener(SubscribedListener listener);

    public void unregisterListeners();

}
