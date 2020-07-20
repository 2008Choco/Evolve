package wtf.choco.evolve.mod;

import com.google.common.base.Preconditions;

import wtf.choco.evolve.util.EvolveResources;
import wtf.choco.evolve.util.ModFile;

import textures.Texture;
import utils.MyFile;

/**
 * Represents a loaded Evolve mod and its associated information.
 *
 * @author Parker Hawke
 */
public final class ModContainer {

    private Texture icon;
    private MyFile iconPath;

    private final Class<?> modClass;
    private final Object modInstance;
    private final ClassLoader classLoader;
    private final String id, version, description, author;

    /**
     * Construct a new ModContainer.
     *
     * @param modInstance an instance of the class annotated with {@literal @Mod}
     * @param classLoader the class loader that loaded this mod
     * @param id the unique String id of the mod
     * @param version the mod version
     * @param description the mod description
     * @param author the mod author
     */
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

    /**
     * Construct a new ModContainer.
     *
     * @param modInstance an instance of the class annotated with {@literal @Mod}
     * @param classLoader the class loader that loaded this mod
     * @param mod the Mod annotation on the modInstance
     */
    public ModContainer(Object modInstance, ClassLoader classLoader, Mod mod) {
        this(modInstance, classLoader, mod.id(), mod.version(), mod.description(), mod.author());
    }

    /**
     * Get this mod's main class.
     *
     * @return the main class
     */
    public Class<?> getModClass() {
        return modClass;
    }

    /**
     * Get an instance of this mod's main class.
     *
     * @return the main class instance
     */
    public Object getModInstance() {
        return modInstance;
    }

    /**
     * Get the {@link ClassLoader} instance that loaded this mod.
     *
     * @return the class loader
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Get this mod's unique String id.
     *
     * @return the mod id
     */
    public String getId() {
        return id;
    }

    /**
     * Get this mod's version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get this mod's description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get this mod's author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set this mod's icon. <strong>NOTE:</strong> This method cannot be called on startup as an
     * OpenGL context has not yet been assigned and Textures cannot be created. Use instead
     * {@link #setIcon(MyFile)} where possible to load the texture when called upon.
     *
     * @see #setIcon(MyFile)
     *
     * @param icon the icon to set
     */
    public void setIcon(Texture icon) {
        this.icon = icon;
    }

    /**
     * Set this mod's icon to the specified path. It is recommended that a {@link ModFile} be used.
     *
     * @see ModFile
     *
     * @param iconPath the path of the icon to set
     */
    public void setIcon(MyFile iconPath) {
        this.iconPath = iconPath;
    }

    /**
     * Get this mod's icon. This method <i>may</i> be blocking if the icon has not yet been
     * fetched after being set by {@link #setIcon(MyFile)}
     *
     * @return the mod icon
     */
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
