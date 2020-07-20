package wtf.choco.evolve.userdev;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public final class LocalLauncher {

    public static void main(String[] astring) throws Exception {
        /* FIXME: This needs some cleaning up. It's not really a great approach to launching locally...
         * I'm not quite sure how to improve this, so contributions are welcome.
         */

        String evolveDirectory = System.getenv("EVOLVE_DIRECTORY");
        if (evolveDirectory == null) {
            System.err.println("Could not find environment variable, EVOLVE_DIRECTORY... is it set?");
            System.err.println("This environment var should point to the root directory of the Evolve project");
            return;
        }

        File sourceFile = LocalLauncher.getSelf();
        String decompileDirectory = evolveDirectory + File.separator + "decompile";
        String compiledVanillaDirectory = decompileDirectory + File.separator + "classes";
        String libsDirectory = evolveDirectory + File.separator + "libs" + File.separator + "*";

        List<String> processArgs = new ArrayList<>();
        processArgs.add("java");
        processArgs.add("-cp");
        processArgs.add(sourceFile.getAbsoluteFile().toString() + ";" + compiledVanillaDirectory + ";" + libsDirectory);
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
