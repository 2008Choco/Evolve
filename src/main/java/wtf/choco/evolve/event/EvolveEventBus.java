package wtf.choco.evolve.event;

import java.util.function.Consumer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public final class EvolveEventBus implements EventBus {

    private final Multimap<Class<? extends Event>, SubscribedListener<?>> listeners = ArrayListMultimap.create();

    EvolveEventBus() { }

    @Override
    public void push(Event event) {
        this.listeners.get(event.getClass()).forEach(l -> l.call(event));
    }

    @Override
    public <T extends Event> SubscribedListener<T> subscribeTo(Class<T> event, Consumer<T> listener) {
        SubscribedListener<T> subscribedListener = new SubscribedListener<>(this, event, listener);
        this.listeners.put(event, subscribedListener);
        return subscribedListener;
    }

    @Override
    public void unregisterListener(SubscribedListener<?> listener) {
        this.listeners.remove(listener.getEventClass(), listener);
    }

    @Override
    public void unregisterListeners() {
        this.listeners.clear();
    }

}
