package wtf.choco.evolve.gui.mods;

import org.lwjgl.util.vector.Vector2f;

import wtf.choco.evolve.mod.ModInfo;

import guiRendering.GuiRenderData;
import guis.GuiComponent;

public final class ModListPanel extends GuiComponent {

    private static final float MOD_COMPONENT_HEIGHT = 0.31F;

    private final ModInfo[] mods;
    private final float ySize;
    private final boolean scrollable;

    public ModListPanel(ModInfo[] mods) {
        this.mods = mods;
        this.ySize = mods.length * MOD_COMPONENT_HEIGHT;
        this.scrollable = ySize > 1.0F;
    }

    public float getYSize() {
        return ySize;
    }

    @Override
    protected void init() {
        super.init();

        for (int i = 0; i < mods.length; i++) {
            this.addComponent(new ModInfoComponent(mods[i]), 0.0F, (i * MOD_COMPONENT_HEIGHT) / ySize, scrollable ? 0.99F : 1.0F, MOD_COMPONENT_HEIGHT / ySize);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f vector2f, Vector2f vector2f1) { }

    @Override
    protected void updateSelf() { }

    @Override
    protected void getGuiTextures(GuiRenderData guirenderdata) { }

}
