package wtf.choco.evolve.mod.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import wtf.choco.evolve.Evolve;
import wtf.choco.evolve.mod.InvalidModException;
import wtf.choco.evolve.mod.Mod;
import wtf.choco.evolve.mod.ModInfo;

public final class ModClassLoader extends URLClassLoader {

    private ModInfo loadedModInfo;

    private final JarFile jarFile;
    private final Manifest manifest;
    private final URL jarURL;
    private final Map<String, Class<?>> loadedClasses = new HashMap<>();
    private final JavaModLoader modLoader;

    public ModClassLoader(ClassLoader parent, File modFile, JavaModLoader modLoader) throws IOException, ClassNotFoundException {
        super(new URL[] { modFile.toURI().toURL() }, parent);
        this.jarFile = new JarFile(modFile);
        this.manifest = jarFile.getManifest();
        this.jarURL = modFile.toURI().toURL();
        this.modLoader = modLoader;

        Logger logger = Evolve.getInstance().getLogger();

        logger.info("Loading mod at " + modFile.getPath());
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
            if (mod == null) {
                continue;
            }

            this.loadedModInfo = new ModInfo(clazz, mod);
            break;
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
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            this.jarFile.close();
        }
    }

    public ModInfo getLoadedModInfo() {
        return loadedModInfo;
    }

    public Set<String> getLoadedClasses() {
        return loadedClasses.keySet();
    }

}
