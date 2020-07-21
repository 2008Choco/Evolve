package wtf.choco.evolve.mod.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.io.IOUtils;

import wtf.choco.evolve.Evolve;
import wtf.choco.evolve.event.Event;
import wtf.choco.evolve.event.EventBus;
import wtf.choco.evolve.event.EventListener;
import wtf.choco.evolve.mod.InvalidModException;
import wtf.choco.evolve.mod.Mod;
import wtf.choco.evolve.mod.ModContainer;

/**
 * Represents a class loader for mods used to share resources between mods at runtime.
 *
 * @author Parker Hawke
 */
public final class ModClassLoader extends URLClassLoader {

    private static final Gson GSON = new Gson();

    ModContainer loadedModInfo;
    final Map<String, Class<?>> loadedClasses = new HashMap<>();

    private final JarFile jarFile;
    private final Manifest manifest;
    private final URL jarURL;

    private final Evolve evolve;
    private final JavaModLoader modLoader;

    /**
     * Construct a new ModClassLoader.
     *
     * @param parent the parent class loader
     * @param modFile the mod file to load
     * @param evolve an instance of Evolve
     * @param modLoader the mod loader instance attempting to load the mod
     *
     * @throws IOException if the file is invalid or inaccessible by the class loader
     * @throws ClassNotFoundException if any mod class failed to load or could not be found
     */
    public ModClassLoader(ClassLoader parent, File modFile, Evolve evolve, JavaModLoader modLoader) throws IOException, ClassNotFoundException {
        super(new URL[] { modFile.toURI().toURL() }, parent);

        Preconditions.checkArgument(evolve != null, "Evolve must not be null");
        Preconditions.checkArgument(modLoader != null, "modLoader must not be null");

        this.jarFile = new JarFile(modFile);
        this.manifest = jarFile.getManifest();
        this.jarURL = modFile.toURI().toURL();

        this.evolve = evolve;
        this.modLoader = modLoader;

        // Load the mod
        this.evolve.getLogger().info("Loading mod at " + modFile.getPath());

        JarEntry modDescriptionFile = jarFile.getJarEntry("mod.json");
        if (modDescriptionFile == null) {
            throw new InvalidModException("Could not find valid mod.json in root directory of mod at " + modFile.getPath());
        }

        // TODO: This can probably handle exceptions a little bit better
        JsonObject modJson = new JsonObject();
        try {
            modJson = GSON.fromJson(new InputStreamReader(getResourceAsStream(modDescriptionFile.getName()), Charset.forName("UTF-8")), JsonObject.class);
        } catch (JsonSyntaxException | JsonIOException e) {
            throw new InvalidModException(e);
        }

        for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();

            // Don't access any non-class entries
            if (!entryName.endsWith(".class")) {
                continue;
            }

            String className = entryName.substring(0, entryName.lastIndexOf('.')).replace("/", ".");
            Class<?> clazz = Class.forName(className, false, this);

            Mod mod = clazz.getAnnotation(Mod.class);
            if (mod != null) {
                if (loadedModInfo != null) {
                    throw new InvalidModException("Mod defines multiple @Mod annotations. This is not legal");
                }

                Object modInstance = null;

                try {
                    modInstance = clazz.getDeclaredConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new InvalidModException(e);
                }

                this.loadedModInfo = new ModContainer(modInstance, this, mod.value(), modJson);
            }

            // Search for listeners
            for (Method method : clazz.getDeclaredMethods()) {
                method.setAccessible(true);

                EventListener listenerAnnotation = method.getAnnotation(EventListener.class);
                if (listenerAnnotation == null) {
                    continue;
                }

                Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length != 1) {
                    throw new InvalidModException("Tried listening for an event (@EventListener) on method " + method.getName() + " but method has missing or illegal event parameters");
                }

                Class<?> eventParameter = parameters[0];
                if (!Event.class.isAssignableFrom(eventParameter)) {
                    throw new InvalidModException("Tried listening for an event (@EventHandler) on method " + method.getName() + " but method is using non-event parameter");
                }

                EventBus.EVOLVE.subscribeTo(eventParameter.asSubclass(Event.class), event -> {
                    try {
                        method.invoke(null, event);
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                });

                this.evolve.getLogger().info("Registered listener in class " + className + " for event " + eventParameter.getName());
            }
        }

        if (loadedModInfo == null) {
            throw new InvalidModException("Missing @Mod annotation while loading mod at " + modFile.getPath());
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = loadedClasses.get(name);

        // Class hasn't been loaded by this class loader, let's load it
        if (clazz == null) {
            String pathToClassFile = name.replace(".", "/").concat(".class"); // foo.bar.Baz -> foo/bar/Baz.class
            JarEntry entry = jarFile.getJarEntry(pathToClassFile);

            if (entry != null) {
                byte[] classBytes;

                try (InputStream inputStream = jarFile.getInputStream(entry)) {
                    classBytes = IOUtils.toByteArray(inputStream);
                } catch (IOException ex) {
                    throw new ClassNotFoundException(name, ex);
                }

                int dot = name.lastIndexOf('.');
                if (dot != -1) {
                    String packageName = name.substring(0, dot);
                    if (getPackage(packageName) == null) {
                        try {
                            if (manifest != null) {
                                this.definePackage(packageName, manifest, jarURL);
                            } else {
                                this.definePackage(packageName, null, null, null, null, null, null, null);
                            }
                        } catch (IllegalArgumentException e) {
                            if (getPackage(packageName) == null) {
                                throw new IllegalStateException("Cannot find package " + packageName);
                            }
                        }
                    }
                }

                clazz = defineClass(name, classBytes, 0, classBytes.length, new CodeSource(jarURL, entry.getCodeSigners()));
            }

            // Final attempt at finding the class
            if (clazz == null) {
                super.findClass(pathToClassFile);
            }
        }

        if (clazz != null) {
            this.loadedClasses.put(name, clazz);
            this.modLoader.defineClass(name, clazz);
        }

        return clazz;
    }

    @Override
    public URL getResource(String name) {
        return findResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return findResources(name);
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            this.jarFile.close();
        }
    }

}
