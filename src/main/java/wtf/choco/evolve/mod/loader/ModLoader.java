package wtf.choco.evolve.mod.loader;

import java.io.File;
import java.io.FilenameFilter;

import wtf.choco.evolve.mod.InvalidModException;
import wtf.choco.evolve.mod.ModInfo;

public interface ModLoader {

    public FilenameFilter getFilePredicate();

    public ModInfo load(File modFile) throws InvalidModException;

    public void unload(ModInfo mod);

}
