package tile_interactive;

import main.GamePanel;

public class IT_Trunk extends InteractiveTile {

    GamePanel gp;

    public IT_Trunk(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        down1 = setUp("/tiles_interactive/trunk", gp.tileSize, gp.tileSize);

        solidArea.width = 0;
        solidArea.height = 0;

    }

}
