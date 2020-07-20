package wtf.choco.evolve.mod.loader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;

import wtf.choco.evolve.Evolve;
import wtf.choco.evolve.mod.InvalidModException;
import wtf.choco.evolve.mod.ModContainer;

public final class JavaModLoader implements ModLoader {

    private static final FilenameFilter FILE_PREDICATE = (file, name) -> name.endsWith(".jar");

    private final Map<String, Class<?>> modClasses = new HashMap<>();
    private final List<ModClassLoader> modClassLoaders = new ArrayList<>();

    private final Evolve evolve;

    public JavaModLoader(Evolve evolve) {
        Preconditions.checkArgument(evolve != null, "Evolve instance must not be null");
        this.evolve = evolve;
    }

    @Override
    public FilenameFilter getFilePredicate() {
        return FILE_PREDICATE;
    }

    @Override
    public ModContainer load(File modFile) throws InvalidModException {
        Preconditions.checkArgument(modFile != null, "modFile must not be null");

        if (!modFile.exists()) {
            throw new InvalidModException(modFile.getPath() + "does not exist, could not load mod");
        }

        ModClassLoader classLoader = null;
        try {
            classLoader = new ModClassLoader(getClass().getClassLoader(), modFile, evolve, this);
        } catch (IOException | ClassNotFoundException e) {
            throw new InvalidModException(e);
        }

        this.modClassLoaders.add(classLoader);
        return classLoader.loadedModInfo;
    }

    @Override
    public void unload(ModContainer mod) {
        Preconditions.checkArgument(mod != null, "modFile must not be null");

        ClassLoader classLoader = mod.getModClass().getClassLoader();
        if (classLoader instanceof ModClassLoader) {
            this.modClassLoaders.remove(classLoader);
            ((ModClassLoader) classLoader).loadedClasses.forEach(modClasses::remove);

            try {
                ((ModClassLoader) classLoader).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void defineClass(String name, Class<?> clazz) {
        this.modClasses.put(name, clazz);
    }

}
