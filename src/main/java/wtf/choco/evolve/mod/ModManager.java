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
    private final Map<String, ModInfo> modsById = new HashMap<>();
    private final List<ModInfo> mods = new ArrayList<>();

    private final Evolve evolve;

    public ModManager(Evolve evolve) {
        this.evolve = evolve;
    }

    public void registerModLoader(String fileExtension, Function<Evolve, ModLoader> modLoader) {
        ModLoader loader = modLoader.apply(evolve);
        if (loader == null) {
            throw new IllegalStateException("Cannot register null mod loader");
        }

        this.modLoaders.put(fileExtension, loader);
    }

    public ModInfo loadMod(File modFile) throws InvalidModException {
        this.ready = false;

        String fileName = modFile.getName();
        String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);

        ModLoader loader = modLoaders.get(fileType);
        if (loader == null) {
            this.evolve.getLogger().warning("Could not load mod " + modFile.getPath() + " - Unknown class loader for file type: " + fileType);
            return null;
        }

        ModInfo modInfo = loader.load(modFile);
        this.modsById.put(modInfo.getId(), modInfo);
        this.mods.add(modInfo);
        this.ready = true;

        return modInfo;
    }

    public ModInfo[] loadMods(File modsDirectory) {
        this.ready = false;

        Preconditions.checkArgument(modsDirectory != null, "modsDirectory must not be null");
        Preconditions.checkState(modsDirectory.isDirectory(), "modsDirectory does not exist or is not a directory");

        Logger logger = evolve.getLogger();
        File[] modFiles = modsDirectory.listFiles();
        List<ModInfo> modInfos = new ArrayList<>(modFiles.length);

        int loadedMods = 0;
        for (int i = 0; i < modFiles.length; i++) {
            ModInfo modInfo = loadMod(modFiles[i]);
            if (modInfo == null) {
                continue;
            }

            modInfos.add(modInfo);
            loadedMods++;
            logger.info("Loaded mod \"" + modInfo.getId() + "\" v" + modInfo.getVersion() + " by " + modInfo.getAuthor());
        }

        logger.info("Successfully loaded " + loadedMods + " mods.");
        this.ready = true;
        return modInfos.toArray(new ModInfo[loadedMods]);
    }

    public void unloadMod(ModInfo modInfo) {
        Preconditions.checkArgument(modInfo != null, "modInfo must not be null");
        Preconditions.checkState(modsById.containsKey(modInfo.getId()), "ModInfo " + modInfo.getId() + " has not been loaded");

        this.modsById.remove(modInfo.getId());
    }

    public ModInfo getMod(String id) {
        return modsById.get(id);
    }

    public boolean isModLoaded(String id) {
        return modsById.containsKey(id);
    }

    public ModInfo[] getMods() {
        return mods.toArray(new ModInfo[mods.size()]);
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
