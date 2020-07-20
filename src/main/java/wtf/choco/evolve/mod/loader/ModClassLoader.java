package wtf.choco.evolve.mod.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.google.common.base.Preconditions;

import org.apache.commons.io.IOUtils;

import wtf.choco.evolve.Evolve;
import wtf.choco.evolve.event.Event;
import wtf.choco.evolve.event.EventBus;
import wtf.choco.evolve.event.EventListener;
import wtf.choco.evolve.mod.InvalidModException;
import wtf.choco.evolve.mod.Mod;
import wtf.choco.evolve.mod.ModContainer;
import wtf.choco.evolve.util.ModFile;

public final class ModClassLoader extends URLClassLoader {

    private ModContainer loadedModInfo;

    private final JarFile jarFile;
    private final Manifest manifest;
    private final URL jarURL;
    private final Map<String, Class<?>> loadedClasses = new HashMap<>();

    private final Evolve evolve;
    private final JavaModLoader modLoader;

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

                this.loadedModInfo = new ModContainer(modInstance, this, mod);
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

        // Try to find mod icon
        JarEntry iconEntry = jarFile.getJarEntry("icon.png");
        if (iconEntry != null) {
            this.loadedModInfo.setIcon(new ModFile(loadedModInfo, iconEntry.getName()));
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

    public ModContainer getLoadedModInfo() {
        return loadedModInfo;
    }

    public Set<String> getLoadedClasses() {
        return loadedClasses.keySet();
    }

}
