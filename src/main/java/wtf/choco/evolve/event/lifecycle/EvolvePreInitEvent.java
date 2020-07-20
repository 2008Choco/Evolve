package wtf.choco.evolve.event.lifecycle;

/**
 * Called when a mod has entered its pre initialization phase. At this point in startup,
 * Equilinox has not yet loaded any of its resources (sounds, menus, etc.). Here, registration
 * of non-gameplay functionality such as keybinds should be registered.
 *
 * @author Parker Hawke
 */
public class EvolvePreInitEvent extends LifecycleEvent { }
