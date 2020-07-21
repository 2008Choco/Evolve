package wtf.choco.evolve.gui.mods;

import com.google.common.base.Preconditions;

import org.lwjgl.util.vector.Vector2f;

import wtf.choco.evolve.mod.ModContainer;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import toolbox.Colour;
import userInterfaces.GuiImage;

public final class ModInfoComponent extends GuiComponent {

    private static final Colour BACKGROUND_COLOUR = ColourPalette.DARK_GREY;
    private static final Colour BACKGROUND_COLOUR_HOVERED = new Colour(0.1F, 0.1F, 0.1F);

    private final GuiImage background;

    private final GuiImage modIcon;
    private final Text modTitle;
    private final Text modDescription;

    public ModInfoComponent(ModContainer mod) {
        Preconditions.checkArgument(mod != null, "mod must not be null");

        this.background = new GuiImage(GuiRepository.BLOCK);
        this.background.getTexture().setOverrideColour(ColourPalette.DARK_GREY);

        this.modIcon = new GuiImage(mod.getIcon());
        this.modTitle = Text.newText(mod.getName() + " v" + mod.getVersion() + " by " + mod.getAuthor()).setFontSize(1.0F).create();
        this.modTitle.setColour(ColourPalette.WHITE);
        this.modDescription = Text.newText(mod.getDescription()).setFontSize(0.85F).create();
        this.modDescription.setColour(ColourPalette.WHITE);
    }

    @Override
    protected void init() {
        super.init();

        this.addComponent(background, 0.0F, 0.0F, 1.0F, 1.0F);

        this.addComponent(modIcon, 0.005F, 0.05F, 0.1F, 0.9F);
        this.addText(modTitle, 0.125F, 0.05F, 0.865F);
        this.addText(modDescription, 0.125F, 0.35F, 0.865F);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f vector2f, Vector2f vector2f1) { }

    @Override
    protected void updateSelf() {
        this.background.getTexture().setOverrideColour(isMouseOver() ? BACKGROUND_COLOUR_HOVERED : BACKGROUND_COLOUR);
    }

    @Override
    protected void getGuiTextures(GuiRenderData guirenderdata) { }

}
