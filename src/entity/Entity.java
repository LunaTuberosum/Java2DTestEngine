package entity;

import main.UtilityTool;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Entity {

    GamePanel gp;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1,
            attackRight2;
    public BufferedImage image, image2, image3;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefultX, solidAreaDefultY;
    public boolean collision = false;
    String dialogues[] = new String[20];

    // STATE

    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;

    // COUNTER

    public int spriteCounter = 0;
    public int actionLockCounter;
    public int invincibleCounter = 0;
    public int shotAvailableCounter = 0;
    public int dyingCounter = 0;
    public int hpBarCounter = 0;

    // CHARACTER ATTIBUTES

    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defence;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;

    // ITEM ATTRIBUTES

    public int value;
    public int attackValue;
    public int defenceValue;
    public String description = "";
    public int useCost;

    // TYPE

    public int type;
    public final int type_player = 0;
    public final int npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;

    public Entity(GamePanel gp) {

        this.gp = gp;
    }

    public void setAction() {
    }

    public void damageReaction() {
    }

    public void speak() {
    }

    public void use(Entity entity) {
    }

    public void checkDrop() {
    }

    public void dropItem(Entity dropedItem) {
    }

    public void update() {

        setAction();

        collisionOn = false;

        // CHECKING COLLISIONS

        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == type_monster && contactPlayer == true) {
            damagePlayer(attack);
        }

        if (collisionOn == false) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;

            }
        }

        spriteCounter++;

        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (invincible) {

            invincibleCounter++;

            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }

    public BufferedImage setUp(String imagePath, int width, int height) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {

            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    public void damagePlayer(int attack) {

        if (gp.player.invincible == false) {

            gp.playSE(6);

            int damage = attack - gp.player.defence;
            if (damage < 0) {
                damage = 0;
            }
            gp.player.life -= damage;

            gp.player.invincible = true;

        }
    }

    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            BufferedImage image = null;

            switch (direction) {
                case "up":
                    if (spriteNum == 1) {
                        image = up1;
                    } else if (spriteNum == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNum == 1) {
                        image = down1;
                    } else if (spriteNum == 2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        image = left1;
                    } else if (spriteNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        image = right1;
                    } else if (spriteNum == 2) {
                        image = right2;
                    }
                    break;
            }

            // Monster HP Bar

            if (type == 2 && hpBarOn) {

                double oneScale = (double) gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }

            }

            if (invincible) {

                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }
            if (dying) {
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);
            changeAlpha(g2, 1f);

        }

    }

    public void dyingAnimation(Graphics2D g2) {

        dyingCounter++;

        int i = 5;

        if (dyingCounter <= i) {

            changeAlpha(g2, 0f);

        } else if (dyingCounter > i && dyingCounter <= i * 2) {

            changeAlpha(g2, 1f);

        } else if (dyingCounter > i * 1 && dyingCounter <= i * 3) {

            changeAlpha(g2, 0f);

        } else if (dyingCounter > i * 3 && dyingCounter <= i * 4) {

            changeAlpha(g2, 1f);

        } else if (dyingCounter > i * 4 && dyingCounter <= i * 5) {

            changeAlpha(g2, 0f);

        } else if (dyingCounter > i * 5 && dyingCounter <= i * 6) {

            changeAlpha(g2, 1f);

        } else if (dyingCounter > i * 6 && dyingCounter <= i * 7) {

            changeAlpha(g2, 0f);

        } else if (dyingCounter > i * 7 && dyingCounter <= i * 8) {

            changeAlpha(g2, 1f);

        } else if (dyingCounter > i * 8) {

            alive = false;
        }

    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

}
