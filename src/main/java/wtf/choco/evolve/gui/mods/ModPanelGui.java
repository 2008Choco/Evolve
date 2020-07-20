package wtf.choco.evolve.gui.mods;

import wtf.choco.evolve.Evolve;
import wtf.choco.evolve.mod.ModContainer;

import fontRendering.Text;
import gameMenu.GameMenuBackground;
import gameMenu.GameMenuGui;
import gameMenu.SecondPanelUi;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import userInterfaces.GuiImage;
import userInterfaces.GuiScrollPanel;

public final class ModPanelGui extends SecondPanelUi {

    private GuiScrollPanel modScrollPanel;

    public ModPanelGui(GameMenuGui gameMenu) {
        super(gameMenu);
    }

    @Override
    protected void init() {
        super.init();

        ModContainer[] mods = Evolve.getInstance().getModManager().getMods();
        this.addTitleBar(mods.length);

        ModListPanel modListPanel = new ModListPanel(mods);
        this.modScrollPanel = new GuiScrollPanel(GameMenuBackground.getStandardColour(), 0.65F) {
            @Override
            protected void init() {
                super.init();
                this.setContents(modListPanel, modListPanel.getYSize());
            }
        };

        this.addComponent(modScrollPanel, 0.1F, 0.15F, 0.8F, 0.8F);
    }

    private void addTitleBar(int modCount) {
        GuiImage header = new GuiImage(GuiRepository.BLOCK);
        header.getTexture().setOverrideColour(ColourPalette.MIDDLE_GREY);
        this.addComponent(header, 0.1F, 0.05F, 0.8F, 0.1F);

        Text title = Text.newText("Mod List (" + modCount + ")").setFontSize(1.35F).create();
        title.setColour(ColourPalette.WHITE);
        header.addText(title, 0.01F, 0.0F, 1.0F);
    }

}
