package wtf.choco.evolve.event;

import wtf.choco.evolve.event.input.KeyPressEvent;

public final class EvolveEventFactory {

    private EvolveEventFactory() { }

    public static boolean handleKeyPressEvent(int key) {
        KeyPressEvent event = new KeyPressEvent(key);
        EventBus.EVOLVE.push(event);
        return !event.isCancelled();
    }

}
