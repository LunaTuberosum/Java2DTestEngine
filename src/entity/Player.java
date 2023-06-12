package entity;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public boolean attackCanceled = false;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefultX = solidArea.x;
        solidAreaDefultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();

    }

    public void setDefultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;

        direction = "down";

        speed = 4;

        // PLAYER STATUS

        level = 1;
        maxLife = 6;
        maxMana = 4;
        life = maxLife;
        mana = maxMana;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 500;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);
        attack = getAttack();
        defence = getDefence();
    }

    public void setDefaltPositions() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;

        direction = "down";
    }

    public void restoreLifeandMana() {

        life = maxLife;
        mana = maxMana;
        invincible = false;
    }

    public void setItems() {

        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
    }

    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        return strength * currentWeapon.attackValue;
    }

    public int getDefence() {
        return dexterity * currentShield.defenceValue;
    }

    public void getPlayerImage() {

        up1 = setUp("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setUp("/player/boy_up_2", gp.tileSize, gp.tileSize);

        down1 = setUp("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setUp("/player/boy_down_2", gp.tileSize, gp.tileSize);

        left1 = setUp("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setUp("/player/boy_left_2", gp.tileSize, gp.tileSize);

        right1 = setUp("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setUp("/player/boy_right_2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage() {

        if (currentWeapon.type == type_sword) {

            attackUp1 = setUp("/player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setUp("/player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);

            attackDown1 = setUp("/player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setUp("/player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);

            attackLeft1 = setUp("/player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setUp("/player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);

            attackRight1 = setUp("/player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setUp("/player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);

        } else if (currentWeapon.type == type_axe) {

            attackUp1 = setUp("/player/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setUp("/player/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);

            attackDown1 = setUp("/player/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setUp("/player/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);

            attackLeft1 = setUp("/player/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setUp("/player/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);

            attackRight1 = setUp("/player/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setUp("/player/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);

        }
    }

    public void update() {

        // ATTACKING
        if (attacking) {

            attacking();
        }
        // MOVEMNET
        else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed
                || keyH.rightPressed || keyH.enterPressed) {

            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // CHECK TILE COLLISION

            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION

            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // CHECK NPC COLLISION

            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // CHECK MONSTER COLLION

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            // CHECK INTERACTIVE TILE COLLISION

            gp.cChecker.checkEntity(this, gp.iTile);

            // CHECK EVENT COLLISION

            gp.eHandler.checkEvent();

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn && !keyH.enterPressed) {
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

            if (keyH.enterPressed && !attackCanceled) {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            keyH.enterPressed = false;

            spriteCounter++;

            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

            if (life <= 0) {
                gp.gameState = gp.gameOverState;
                gp.ui.commandNum = -1;
                gp.stopMusic();
                gp.playSE(12);
            }

        }

        if (keyH.shotKeyPressed && !projectile.alive && shotAvailableCounter >= 30 && projectile.haveResource(this)) {

            // SET DEFAULT POSITION, DIRECTION, AND USER
            projectile.set(worldX, worldY, direction, true, this);

            // SUBTRACT THE COST (MANA, AMMO, ECT...)
            projectile.subtractResource(this);

            // ADD IT TO THE LIST
            gp.projectileList.add(projectile);

            shotAvailableCounter = 0;

            gp.playSE(10);
        }

        if (invincible) {

            invincibleCounter++;

            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
        if (life > maxLife) {
            life = maxLife;
        }
        if (mana > maxMana) {
            mana = maxMana;
        }

    }

    public void attacking() {

        spriteCounter++;

        if (spriteCounter <= 5) {

            spriteNum = 1;

        } else if (spriteCounter > 5 && spriteCounter <= 25) {

            spriteNum = 2;

            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            switch (direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
            }

            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, this.attack);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        } else if (spriteCounter > 25) {

            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void pickUpObject(int index) {

        if (index != 999) {

            // PICKUP ONLY ITEMS
            if (gp.obj[gp.currentMap][index].type == type_pickupOnly) {
                gp.obj[gp.currentMap][index].use(this);
                gp.obj[gp.currentMap][index] = null;
            }

            // INVENTORY ITEMS
            else {
                String text;

                if (inventory.size() != maxInventorySize) {

                    inventory.add(gp.obj[gp.currentMap][index]);
                    gp.playSE(1);
                    text = "Got a " + gp.obj[gp.currentMap][index].name + "!";
                } else {
                    text = "You cannot carry any more!";
                }

                gp.ui.addMessage(text);
                gp.obj[gp.currentMap][index] = null;
            }

        }
    }

    public void interactNPC(int index) {

        if (keyH.enterPressed) {
            if (index != 999) {

                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[gp.currentMap][index].speak();

            }
        }
    }

    public void contactMonster(int index) {

        if (index != 999) {
            if (!invincible && !gp.monster[gp.currentMap][index].dying) {

                gp.playSE(6);

                int damage = gp.monster[gp.currentMap][index].attack - defence;
                if (damage < 0) {
                    damage = 0;
                }

                life -= damage;
                invincible = true;
            }
        }
    }

    public void damageMonster(int index, int attack) {

        if (index != 999) {

            if (!gp.monster[gp.currentMap][index].invincible) {

                gp.playSE(5);

                int damage = attack - gp.monster[gp.currentMap][index].defence;
                if (damage < 0) {
                    damage = 0;
                }

                gp.monster[gp.currentMap][index].life -= damage;
                gp.ui.addMessage(damage + " damage!");

                gp.monster[gp.currentMap][index].invincible = true;
                gp.monster[gp.currentMap][index].damageReaction();

                if (gp.monster[gp.currentMap][index].life <= 0) {

                    gp.monster[gp.currentMap][index].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][index].name + "!");
                    gp.ui.addMessage("Exp + " + gp.monster[gp.currentMap][index].exp);
                    exp += gp.monster[gp.currentMap][index].exp;
                    checkLevelUp();
                }
            }
        }
    }

    public void damageInteractiveTile(int index) {

        if (index != 999 && gp.iTile[gp.currentMap][index].destructable
                && gp.iTile[gp.currentMap][index].isCorrectItem(this)
                && !gp.iTile[gp.currentMap][index].invincible) {

            gp.iTile[gp.currentMap][index].playSE();
            gp.iTile[gp.currentMap][index].life--;
            gp.iTile[gp.currentMap][index].invincible = true;

            // GENERATE PARTICLE
            generateParticle(gp.iTile[gp.currentMap][index], gp.iTile[gp.currentMap][index]);

            if (gp.iTile[gp.currentMap][index].life == 0) {
                gp.iTile[gp.currentMap][index] = gp.iTile[gp.currentMap][index].getDestroyedForm();

            }
        }
    }

    public void checkLevelUp() {

        if (exp >= nextLevelExp) {

            level++;
            nextLevelExp *= 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defence = getDefence();

            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are level " + level + " now!\nYou feel stronger!";

        }
    }

    public void selectItem() {

        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        if (itemIndex < inventory.size()) {

            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.type == type_sword || selectedItem.type == type_axe) {

                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();

            } else if (selectedItem.type == type_shield) {

                currentShield = selectedItem;
                defence = getDefence();

            } else if (selectedItem.type == type_consumable) {

                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {

            case "up":

                // MOVING
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = up1;
                    } else if (spriteNum == 2) {
                        image = up2;
                    }
                }
                // ATTACKING
                else {
                    tempScreenY = screenY - gp.tileSize;
                    if (spriteNum == 1) {
                        image = attackUp1;
                    } else if (spriteNum == 2) {
                        image = attackUp2;
                    }
                }
                break;

            case "down":

                // MOVING
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = down1;
                    } else if (spriteNum == 2) {
                        image = down2;
                    }
                }
                // ATTACKING
                else {
                    if (spriteNum == 1) {
                        image = attackDown1;
                    } else if (spriteNum == 2) {
                        image = attackDown2;
                    }
                }
                break;

            case "left":

                // MOVING
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = left1;
                    } else if (spriteNum == 2) {
                        image = left2;
                    }
                }
                // ATTACKING
                else {
                    tempScreenX = screenX - gp.tileSize;
                    if (spriteNum == 1) {
                        image = attackLeft1;
                    } else if (spriteNum == 2) {
                        image = attackLeft2;
                    }
                }
                break;

            case "right":

                // MOVING
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = right1;
                    } else if (spriteNum == 2) {
                        image = right2;
                    }
                }
                // ATTACKING
                else {
                    if (spriteNum == 1) {
                        image = attackRight1;
                    } else if (spriteNum == 2) {
                        image = attackRight2;
                    }
                }
                break;
        }

        if (invincible) {

            changeAlpha(g2, 0.3f);
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        changeAlpha(g2, 1f);

    }
}
