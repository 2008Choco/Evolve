package wtf.choco.evolve.event;

import java.util.function.Consumer;

public interface EventBus {

    public static final EventBus EVOLVE = new EvolveEventBus();

    public void push(Event event);

    public <T extends Event> SubscribedListener<T> subscribeTo(Class<T> event, Consumer<T> listener);

    public void unregisterListener(SubscribedListener<?> listener);

    public void unregisterListeners();

}
