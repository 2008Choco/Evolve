package wtf.choco.evolve.util;

import textures.Texture;
import utils.MyFile;

/**
 * Various resources and textures provided by the Evolve modding framework not
 * otherwise provided by vanilla Equilinox.
 *
 * @author Parker Hawke
 */
public final class EvolveResources {

    public static final Texture EVOLVE_LOGO = loadGuiTexture("evolve_logo.png");
    public static final Texture UNKNOWN_MOD_ICON = loadGuiTexture("unknown_mod_icon.png");

    private EvolveResources() { }

    private static Texture loadGuiTexture(String name) {
        return Texture.newTexture(new MyFile("textures/gui", name)).noFiltering().clampEdges().create();
    }

}
