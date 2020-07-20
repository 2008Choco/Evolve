package wtf.choco.evolve.mod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;

import wtf.choco.evolve.Evolve;
import wtf.choco.evolve.mod.loader.ModLoader;

public final class ModManager {

    private boolean ready = false;

    private final Map<String, ModLoader> modLoaders = new HashMap<>();
    private final Map<String, ModContainer> modsById = new HashMap<>();
    private final List<ModContainer> mods = new ArrayList<>();

    private final Evolve evolve;

    public ModManager(Evolve evolve) {
        this.evolve = evolve;
    }

    public void registerModLoader(String fileExtension, Function<Evolve, ModLoader> modLoader) {
        Preconditions.checkArgument(fileExtension != null, "fileExtension must not be null");
        Preconditions.checkArgument(modLoader != null, "modLoader Function must not be null");

        ModLoader loader = modLoader.apply(evolve);
        if (loader == null) {
            throw new IllegalStateException("Cannot register null mod loader");
        }

        this.modLoaders.put(fileExtension, loader);
    }

    public ModContainer loadMod(File modFile) throws InvalidModException {
        Preconditions.checkArgument(modFile != null, "Cannot load null mod file");

        this.ready = false;

        String fileName = modFile.getName();
        String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);

        ModLoader loader = modLoaders.get(fileType);
        if (loader == null) {
            this.evolve.getLogger().warning("Could not load mod " + modFile.getPath() + " - Unknown class loader for file type: " + fileType);
            return null;
        }

        ModContainer modContainer = loader.load(modFile);
        this.modsById.put(modContainer.getId(), modContainer);
        this.mods.add(modContainer);
        this.ready = true;

        return modContainer;
    }

    public ModContainer[] loadMods(File modsDirectory) {
        Preconditions.checkArgument(modsDirectory != null, "modsDirectory must not be null");
        Preconditions.checkState(modsDirectory.isDirectory(), "modsDirectory does not exist or is not a directory");

        this.ready = false;

        Logger logger = evolve.getLogger();
        File[] modFiles = modsDirectory.listFiles();
        List<ModContainer> modInfos = new ArrayList<>(modFiles.length);

        int loadedMods = 0;
        for (int i = 0; i < modFiles.length; i++) {
            ModContainer modContainer = loadMod(modFiles[i]);
            if (modContainer == null) {
                continue;
            }

            modInfos.add(modContainer);
            loadedMods++;
            logger.info("Loaded mod \"" + modContainer.getId() + "\" v" + modContainer.getVersion() + " by " + modContainer.getAuthor());
        }

        logger.info("Successfully loaded " + loadedMods + " mods.");
        this.ready = true;
        return modInfos.toArray(new ModContainer[loadedMods]);
    }

    public void unloadMod(ModContainer modContainer) {
        Preconditions.checkArgument(modContainer != null, "modContainer must not be null");
        Preconditions.checkState(modsById.containsKey(modContainer.getId()), "Mod with ID " + modContainer.getId() + " has not been loaded");

        this.modsById.remove(modContainer.getId());
    }

    public ModContainer getMod(String id) {
        return modsById.get(id);
    }

    public boolean isModLoaded(String id) {
        return modsById.containsKey(id);
    }

    public ModContainer[] getMods() {
        return mods.toArray(new ModContainer[mods.size()]);
    }

    public void clearMods() {
        this.modLoaders.clear();
        this.modsById.clear();
        this.mods.clear();
    }

    public boolean isReady() {
        return ready;
    }

}
