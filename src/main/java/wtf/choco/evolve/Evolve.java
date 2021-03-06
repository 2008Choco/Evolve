package wtf.choco.evolve;

import java.io.File;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import wtf.choco.evolve.event.EventBus;
import wtf.choco.evolve.mod.ModManager;
import wtf.choco.evolve.mod.loader.JavaModLoader;

import main.MainApp;

/**
 * The Evolve modding framework.
 */
public final class Evolve {

    private static final String LOGGER_FORMAT = "[%1$tF %1$tT] [%2$s] %3$s %n";

    private static final Evolve INSTANCE = new Evolve();

    private final Logger logger = Logger.getLogger("Evolve");
    private final File modsDirectory = new File("./mods");

    private ModManager modManager;

    private Evolve() {
        this.logger.setUseParentHandlers(false);

        ConsoleHandler loggerConsoleHandler = new ConsoleHandler();
        loggerConsoleHandler.setFormatter(new SimpleFormatter() {

            @Override
            public synchronized String format(LogRecord record) {
                return String.format(LOGGER_FORMAT, new Date(record.getMillis()), record.getLevel().getLocalizedName(), record.getMessage());
            }

        });

        this.logger.addHandler(loggerConsoleHandler);
    }

    void init() {
        this.logger.info("Loading Evolve modding framework for Equilinox " + MainApp.VERSION_STRING);

        this.modManager = new ModManager(this);
        this.modManager.registerModLoader("jar", JavaModLoader::new);

        if (modsDirectory.mkdirs()) {
            this.logger.info("Generated mods directory");
        }
    }

    void shutdown() {
        this.logger.info("Handling mod shutdown...");

        EventBus.EVOLVE.unregisterListeners();
        this.modManager.clearMods();

        this.logger.info("Goodbye! Thanks for modding!");
    }

    /**
     * Get Evolve's Logger instance. It is recommended that mods avoid using this logger and
     * instead create their own.
     *
     * @return the Evolve logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Get the directory in which mods should be loaded.
     *
     * @return the mods directory
     */
    public File getModsDirectory() {
        return modsDirectory;
    }

    /**
     * Get the {@link ModManager} instance.
     *
     * @return the mod manager
     */
    public ModManager getModManager() {
        return modManager;
    }

    /**
     * Get the Evolve instance.
     *
     * @return this instance
     */
    public static Evolve getInstance() {
        return INSTANCE;
    }

}
