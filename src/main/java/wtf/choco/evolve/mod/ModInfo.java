package wtf.choco.evolve.mod;

import wtf.choco.evolve.util.Check;

public final class ModInfo {

    private final Class<?> modClass;
    private final String id, version, description, author;

    public ModInfo(Class<?> modClass, String id, String version, String description, String author) {
        Check.argument(modClass != null, "modClass must not be null");
        Check.argument(id != null && !id.isEmpty(), "id cannot be null or empty");
        Check.argument(version != null && !version.isEmpty(), "version cannot be null or empty");

        this.modClass = modClass;
        this.id = id;
        this.version = version;
        this.description = description;
        this.author = author;
    }

    public ModInfo(Class<?> modClass, Mod mod) {
        this(modClass, mod.id(), mod.version(), mod.description(), mod.author());
    }

    public Class<?> getModClass() {
        return modClass;
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

}
