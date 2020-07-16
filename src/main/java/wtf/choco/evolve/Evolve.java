package wtf.choco.evolve;

import java.io.File;
import java.util.logging.Logger;

import wtf.choco.evolve.mod.ModInfo;
import wtf.choco.evolve.mod.ModManager;
import wtf.choco.evolve.mod.loader.JavaModLoader;

import main.MainApp;

public final class Evolve {

    private static final Evolve INSTANCE = new Evolve();

    private final Logger logger = Logger.getLogger("Evolve");
    private final File modsDirectory = new File("./mods");

    private ModManager modManager;

    Evolve() { }

    void init() {
        this.logger.info("Loading Evolve modding framework for Equilinox version " + MainApp.VERSION_STRING);

        this.modManager = new ModManager(this);
        this.modManager.registerModLoader("jar", JavaModLoader::new);

        if (modsDirectory.mkdirs()) {
            this.logger.info("Generated mods directory");
        }

        this.modManager.loadMods(modsDirectory);

        // FIXME: Temporary debug information. Should be moved into the plugin startup logic
        for (ModInfo modInfo : modManager.getMods()) {
            System.out.println("Successfully loaded mod with ID " + modInfo.getId() + " v" + modInfo.getVersion());
            System.out.println("    Description: \"" + modInfo.getDescription() + "\"");
            System.out.println("    Author: " + modInfo.getAuthor());
            System.out.println("    Mod Class: " + modInfo.getModClass().getName());
        }
    }

    void shutdown() {
        this.logger.info("Handling mod shutdown...");
        this.modManager.clearMods();

        this.logger.info("Goodbye! Thanks for modding!");
    }

    public Logger getLogger() {
        return logger;
    }

    public File getModsDirectory() {
        return modsDirectory;
    }

    public static Evolve getInstance() {
        return INSTANCE;
    }

}
