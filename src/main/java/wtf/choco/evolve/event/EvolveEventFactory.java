package wtf.choco.evolve.event;

import wtf.choco.evolve.event.input.KeyPressEvent;
import wtf.choco.evolve.event.lifecycle.EvolvePostInitEvent;
import wtf.choco.evolve.event.lifecycle.EvolvePreInitEvent;
import wtf.choco.evolve.event.lifecycle.EvolveShutdownEvent;

/**
 * A utility class for Evolve to more easily call events to the Evolve event bus.
 * This class is meant for internal use and should be avoided by mods.
 *
 * @author Parker Hawke
 */
public final class EvolveEventFactory {

    // Methods here need not be documented. They're not meant for public use anyways
    private EvolveEventFactory() { }

    public static boolean handleKeyPressEvent(int key) {
        KeyPressEvent event = new KeyPressEvent(key);
        EventBus.EVOLVE.push(event);
        return !event.isCancelled();
    }

    public static void callEvolvePreInitEvent() {
        EventBus.EVOLVE.push(new EvolvePreInitEvent());
    }

    public static void callEvolvePostInitEvent() {
        EventBus.EVOLVE.push(new EvolvePostInitEvent());
    }

    public static void callEvolveShutdownEvent() {
        EventBus.EVOLVE.push(new EvolveShutdownEvent());
    }

}
