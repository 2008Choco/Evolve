package wtf.choco.evolve.gui.mods;

import wtf.choco.evolve.Evolve;

import fontRendering.Text;
import gameMenu.GameMenuBackground;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import userInterfaces.GuiImage;
import userInterfaces.GuiPanel;

public final class ModListPanel extends GuiPanel {

    public ModListPanel() {
        super(GameMenuBackground.getStandardColour(), 0.65F);
        this.addTitleBarImage();
    }

    private void addTitleBarImage() {
        GuiImage header = new GuiImage(GuiRepository.BLOCK);
        header.getTexture().setOverrideColour(ColourPalette.MIDDLE_GREY);
        super.addComponent(header, 0.0F, 0.0F, 1.0F, 0.1F);

        Text title = Text.newText("Mod List (" + Evolve.getInstance().getModManager().getMods().length + ")").setFontSize(1.35F).create();
        title.setColour(ColourPalette.WHITE);
        header.addText(title, 0.01F, 0.0F, 1.0F);
    }

}
