package wtf.choco.evolve.mod.loader;

import java.io.File;
import java.io.FilenameFilter;

import wtf.choco.evolve.mod.InvalidModException;
import wtf.choco.evolve.mod.ModContainer;

public interface ModLoader {

    public FilenameFilter getFilePredicate();

    public ModContainer load(File modFile) throws InvalidModException;

    public void unload(ModContainer mod);

}
