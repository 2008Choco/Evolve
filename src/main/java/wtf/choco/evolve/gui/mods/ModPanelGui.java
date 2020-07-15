package wtf.choco.evolve.gui.mods;

import gameMenu.GameMenuGui;
import gameMenu.SecondPanelUi;

public class ModPanelGui extends SecondPanelUi {

    public ModPanelGui(GameMenuGui gameMenu) {
        super(gameMenu);
    }

    @Override
    protected void init() {
        super.init();

        this.addComponent(new ModListPanel(), 0.13F, 0.1F, 0.8F, 0.85F);
    }

}
