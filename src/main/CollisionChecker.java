package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {

        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomtWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomtWorldY / gp.tileSize;

        int tileNum1 = 0;
        int tileNum2 = 0;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNumber[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNumber[gp.currentMap][entityRightCol][entityTopRow];
                break;

            case "down":
                entityBottomRow = (entityBottomtWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNumber[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNumber[gp.currentMap][entityRightCol][entityBottomRow];
                break;

            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNumber[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNumber[gp.currentMap][entityLeftCol][entityBottomRow];
                break;

            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNumber[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNumber[gp.currentMap][entityRightCol][entityBottomRow];
                break;
        }

        if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
            entity.collisionOn = true;
        }
    }

    public int checkObject(Entity entity, boolean player) {

        int index = 999;

        for (int i = 0; i < gp.obj[1].length; i++) {

            if (gp.obj[gp.currentMap][i] != null) {

                // Get entity's solid area postion

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's solid area position

                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldX
                        + gp.obj[gp.currentMap][i].solidArea.x;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldY
                        + gp.obj[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;

                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;

                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;

                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                    if (gp.obj[gp.currentMap][i].collision == true) {

                        entity.collisionOn = true;
                    }
                    if (player == true) {

                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefultX;
                entity.solidArea.y = entity.solidAreaDefultY;

                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefultX;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefultY;

            }
        }

        return index;
    }

    // NPC OR MONSTER
    public int checkEntity(Entity entity, Entity[][] target) {

        int index = 999;

        for (int i = 0; i < target[1].length; i++) {

            if (target[gp.currentMap][i] != null) {

                // Get entity's solid area postion

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the other entity's solid area position

                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX
                        + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY
                        + target[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;

                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;

                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;

                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    if (target[gp.currentMap][i] != entity) {

                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefultX;
                entity.solidArea.y = entity.solidAreaDefultY;

                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefultY;

            }
        }

        return index;
    }

    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

        // Get entity's solid area postion
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        // Get the player's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;

            case "down":
                entity.solidArea.y += entity.speed;
                break;

            case "left":
                entity.solidArea.x -= entity.speed;
                break;

            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }

        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.solidArea.x = entity.solidAreaDefultX;
        entity.solidArea.y = entity.solidAreaDefultY;

        gp.player.solidArea.x = gp.player.solidAreaDefultX;
        gp.player.solidArea.y = gp.player.solidAreaDefultY;

        return contactPlayer;
    }
}
