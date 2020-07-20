package wtf.choco.evolve.util;

import java.io.InputStream;
import java.net.URL;

import wtf.choco.evolve.mod.ModContainer;

import utils.MyFile;

public class ModFile extends MyFile {

    private final ModContainer mod;

    public ModFile(ModContainer mod, String path) {
        super(path);
        this.mod = mod;
    }

    @Override
    public InputStream getInputStream() {
        return this.mod.getClassLoader().getResourceAsStream(getPath());
    }

    @Override
    public URL getUrl() {
        return this.mod.getClassLoader().getResource(getPath());
    }

}
