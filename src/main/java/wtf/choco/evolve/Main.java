package wtf.choco.evolve;

import main.MainApp;

public final class Main {

    public static void main(String[] args) {
        Evolve.getInstance().init();

        // Start the vanilla game
        MainApp.main(args);
    }

}
