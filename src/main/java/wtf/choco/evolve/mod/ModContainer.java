package wtf.choco.evolve.mod;

import com.google.common.base.Preconditions;

import wtf.choco.evolve.util.EvolveResources;

import textures.Texture;
import utils.MyFile;

public final class ModContainer {

    private Texture icon;
    private MyFile iconPath;

    private final Class<?> modClass;
    private final Object modInstance;
    private final ClassLoader classLoader;
    private final String id, version, description, author;

    public ModContainer(Object modInstance, ClassLoader classLoader, String id, String version, String description, String author) {
        Preconditions.checkArgument(modInstance != null, "modInstance must not be null");
        Preconditions.checkArgument(classLoader != null, "classLoader must not be null");
        Preconditions.checkArgument(id != null && !id.isEmpty(), "id cannot be null or empty");
        Preconditions.checkArgument(version != null && !version.isEmpty(), "version cannot be null or empty");

        this.modInstance = modInstance;
        this.modClass = modInstance.getClass();
        this.classLoader = classLoader;
        this.id = id;
        this.version = version;
        this.description = description;
        this.author = author;
    }

    public ModContainer(Object modInstance, ClassLoader classLoader, Mod mod) {
        this(modInstance, classLoader, mod.id(), mod.version(), mod.description(), mod.author());
    }

    public Class<?> getModClass() {
        return modClass;
    }

    public Object getModInstance() {
        return modInstance;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public void setIcon(Texture icon) {
        this.icon = icon;
    }

    public void setIcon(MyFile iconPath) {
        this.iconPath = iconPath;
    }

    public Texture getIcon() {
        if (iconPath != null) {
            this.icon = Texture.newTexture(iconPath).clampEdges().noFiltering().create();
            this.iconPath = null;
        }

        if (icon == null) {
            this.icon = EvolveResources.UNKNOWN_MOD_ICON;
        }

        return icon;
    }

}
