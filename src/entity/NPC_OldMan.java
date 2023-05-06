package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_OldMan extends Entity {

    public NPC_OldMan(GamePanel gp) {

        super(gp);

        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }

    public void getImage() {

        up1 = setUp("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
        up2 = setUp("/npc/oldman_up_2", gp.tileSize, gp.tileSize);

        down1 = setUp("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setUp("/npc/oldman_down_2", gp.tileSize, gp.tileSize);

        left1 = setUp("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
        left2 = setUp("/npc/oldman_left_2", gp.tileSize, gp.tileSize);

        right1 = setUp("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
        right2 = setUp("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue() {

        dialogues[0] = "Hello, gal.";
        dialogues[1] = "So you've come to this island to find the \ntreasure?";
        dialogues[2] = "I used to be a great wiazrt but now... \nI'm a bit too old for taking an adventure.";
        dialogues[3] = "Well, good luck for you.";
    }

    public void setAction() {

        actionLockCounter++;

        if (actionLockCounter == 120) {

            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else if (i <= 100) {
                direction = "right";
            }

            actionLockCounter = 0;
        }

    }

    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

}
