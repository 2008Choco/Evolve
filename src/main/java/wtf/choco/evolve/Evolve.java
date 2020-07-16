package wtf.choco.evolve;

import java.io.File;
import java.util.logging.Logger;

import wtf.choco.evolve.mod.ModInfo;
import wtf.choco.evolve.mod.loader.JavaModLoader;
import wtf.choco.evolve.mod.loader.ModLoader;

import main.MainApp;

public final class Evolve {

    private static final Evolve INSTANCE = new Evolve();

    private final Logger logger = Logger.getLogger("Evolve");
    private final File modsDirectory = new File("./mods");

    private ModLoader modLoader;

    Evolve() { }

    void init() {
        this.logger.info("Loading Evolve modding framework for Equilinox version " + MainApp.VERSION_STRING);

        this.modLoader = new JavaModLoader();

        if (modsDirectory.mkdirs()) {
            this.logger.info("Generated mods directory");
        }

        for (File modFile : modsDirectory.listFiles(modLoader.getFilePredicate())) {
            ModInfo modInfo = modLoader.load(modFile);
            if (modInfo == null) {
                this.logger.warning("Failed to load mod at " + modFile.getPath());
                continue;
            }

            // TODO: Save the mod info to a manager
            System.out.println("Successfully loaded mod with ID " + modInfo.getId() + " v" + modInfo.getVersion());
            System.out.println("    Description: \"" + modInfo.getDescription() + "\"");
            System.out.println("    Author: " + modInfo.getAuthor());
            System.out.println("    Mod Class: " + modInfo.getClass().getName());
        }
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
