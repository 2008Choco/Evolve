package wtf.choco.evolve.util;

import textures.Texture;
import utils.MyFile;

public final class EvolveResources {

    public static final Texture EVOLVE_LOGO = loadGuiTexture("evolve_logo.png");

    private EvolveResources() { }

    private static Texture loadGuiTexture(String name) {
        return Texture.newTexture(new MyFile("textures/gui", name)).noFiltering().clampEdges().create();
    }

}
