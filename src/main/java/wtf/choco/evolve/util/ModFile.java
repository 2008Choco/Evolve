package wtf.choco.evolve.util;

import java.io.InputStream;
import java.net.URL;

import com.google.common.base.Preconditions;

import wtf.choco.evolve.mod.ModContainer;

import utils.MyFile;

public class ModFile extends MyFile {

    private final ModContainer mod;
    private final String path;

    public ModFile(ModContainer mod, String path) {
        super(path);

        Preconditions.checkArgument(mod != null, "mod cannot be null");
        this.mod = mod;
        this.path = path;
    }

    @Override
    public InputStream getInputStream() {
        return this.mod.getClassLoader().getResourceAsStream(getRawPath());
    }

    @Override
    public URL getUrl() {
        return this.mod.getClassLoader().getResource(getRawPath());
    }

    public String getRawPath() {
        return path;
    }

}
