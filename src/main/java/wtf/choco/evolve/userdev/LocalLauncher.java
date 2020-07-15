package wtf.choco.evolve.userdev;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public final class LocalLauncher {

    public static void main(String[] astring) throws Exception {
        String decompileDirectory = System.getenv("EQUILINOX_DECOMP_DIRECTORY");
        if (decompileDirectory == null) {
            System.err.println("Could not find environment variable, EQUILINOX_DECOMP_DIRECTORY... is it set?");
            System.err.println("This environment var should point to the root directory of the decompiled source");
            return;
        }

        File sourceFile = LocalLauncher.getSelf();
        String compiledVanillaDirectory = decompileDirectory + File.separator + "classes";

        List<String> processArgs = new ArrayList<>();
        processArgs.add("java");
        processArgs.add("-cp");
        processArgs.add(sourceFile.getAbsoluteFile().toString() + ";" + compiledVanillaDirectory);
        processArgs.add("-Djava.library.path=" + decompileDirectory); // Natives should be located here
        processArgs.add("wtf.choco.evolve.Main");

        ProcessBuilder processbuilder = new ProcessBuilder(processArgs);
        processbuilder.redirectErrorStream(true);
        Process process = processbuilder.inheritIO().start();

        process.waitFor();
    }

    private static File getSelf() {
        try {
            return new File(LocalLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException urisyntaxexception) {
            urisyntaxexception.printStackTrace();
            return null;
        }
    }

}
