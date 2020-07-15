package wtf.choco.evolve;

import java.util.logging.Logger;

import main.MainApp;

public final class Main {

    public static final Logger LOGGER = Logger.getLogger("Evolve");

    public static void main(String[] args) {
        LOGGER.info("Loading Evolve modding framework for Equilinox version " + MainApp.VERSION_STRING);

        // Start the vanilla game
        MainApp.main(args);
    }

}
