package wtf.choco.evolve.event;

/**
 * Represents an event that occurred in Evolve or Equilinox. Events may be listened to
 * using either the {@link EventListener} annotation upon a static method or using the
 * {@link EventBus#subscribeTo(Class, java.util.function.Consumer)} method.
 * <p>
 * This event cannot be listened to.
 *
 * @see EventListener
 * @see EventBus#EVOLVE
 *
 * @author Parker Hawke
 */
public abstract class Event { }
