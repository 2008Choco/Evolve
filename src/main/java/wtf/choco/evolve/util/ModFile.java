package wtf.choco.evolve.util;

import java.io.InputStream;
import java.net.URL;

import com.google.common.base.Preconditions;

import wtf.choco.evolve.mod.ModContainer;

import utils.MyFile;

/**
 * A more specific implementation of {@link MyFile} to point towards a mod-exclusive
 * file path.
 *
 * @author Parker Hawke
 */
public class ModFile extends MyFile {

    private final ModContainer mod;
    private final String path;

    /**
     * Construct a new ModFile instance.
     *
     * @param mod the mod. Must not be null
     * @param path the relative path to the file (not prepended by "/"). Must not be null
     */
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

    /**
     * Get the raw, non-modified path passed to the constructor. Note that this should be
     * used mostly internally by Evolve as this provides more accurate access to mod files.
     *
     * @return the raw path
     */
    public String getRawPath() {
        return path;
    }

}
