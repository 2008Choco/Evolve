package wtf.choco.evolve.event;

import java.lang.reflect.Modifier;
import java.util.function.Consumer;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public final class EvolveEventBus implements EventBus {

    private final Multimap<Class<? extends Event>, SubscribedListener<?>> listeners = ArrayListMultimap.create();

    EvolveEventBus() { }

    @Override
    public void push(Event event) {
        Preconditions.checkArgument(event != null, "Cannot push null event to bus");
        this.listeners.get(event.getClass()).forEach(l -> l.call(event));
    }

    @Override
    public <T extends Event> SubscribedListener<T> subscribeTo(Class<T> event, Consumer<T> listener) {
        Preconditions.checkArgument(event != null, "event class must not be null");
        Preconditions.checkArgument(listener != null, "Listener must not be null");
        Preconditions.checkArgument((event.getClass().getModifiers() & Modifier.ABSTRACT) == 0, "Cannot listen to abstract event, " + event.getClass().getName());

        SubscribedListener<T> subscribedListener = new SubscribedListener<>(this, event, listener);
        this.listeners.put(event, subscribedListener);
        return subscribedListener;
    }

    @Override
    public void unregisterListener(SubscribedListener<?> listener) {
        Preconditions.checkArgument(listener != null, "Cannot unregister null listener");
        this.listeners.remove(listener.getEventClass(), listener);
    }

    @Override
    public void unregisterListeners() {
        this.listeners.clear();
    }

}
