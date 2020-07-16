package wtf.choco.evolve;

import main.MainApp;

public final class Main {

    public static void main(String[] args) {
        Evolve evolve = Evolve.getInstance();
        evolve.init();

        // Start the vanilla game
        MainApp.main(args);

        // Perform cleanup of mods
        // FIXME: This should probably be called much earlier so mods can still have access to necessary Equilinox data on shutdown
        evolve.shutdown();
    }

}
