package wtf.choco.evolve;

import main.MainApp;

public final class Main {

    public static void main(String[] args) {
        Evolve evolve = Evolve.getInstance();
        evolve.init();

        // Start the vanilla game
        MainApp.main(args);

        // Perform cleanup of Evolve (this is post mod shutdown)
        evolve.shutdown();
    }

}
