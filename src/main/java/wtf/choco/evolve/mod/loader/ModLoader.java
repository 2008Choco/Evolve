package wtf.choco.evolve.mod.loader;

import java.io.File;
import java.io.FilenameFilter;

import wtf.choco.evolve.mod.InvalidModException;
import wtf.choco.evolve.mod.ModContainer;

/**
 * Represents a class capable of loading a mod as a {@link ModContainer}.
 *
 * @author Parker Hawke
 */
public interface ModLoader {

    /**
     * Get the predicate used to filter which files are considered valid for this mod loader.
     *
     * @return the file predicate
     */
    public FilenameFilter getFilePredicate();

    /**
     * Attempt to load the mod located at the given file.
     *
     * @param modFile the mod file to load
     *
     * @return the loaded mod
     *
     * @throws InvalidModException if the mod failed to load for any reason
     */
    public ModContainer load(File modFile) throws InvalidModException;

    /**
     * Unload the given mod.
     *
     * @param mod the mod to unload
     */
    public void unload(ModContainer mod);

}
