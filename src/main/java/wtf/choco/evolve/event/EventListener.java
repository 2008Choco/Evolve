package wtf.choco.evolve.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a method as an event listener. Any method on which this annotation is present will
 * be automatically registered to the {@link EventBus#EVOLVE Evolve EventBus}.
 * <pre>
 * {@literal @EventListener}
 * public static void listen(EvolvePreInitEvent event) {
 *     System.out.println("Hello pre init!");
 * }
 * </pre>
 *
 * @author Parker Hawke
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener { }
