package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

import static main.JunctionChecker.*;

public class Car extends Entity implements Serializable {
    public transient GamePanel gp;
    private boolean canMove = true;

    public Car(GamePanel gp, double kmhSpeed, String direction) {
        this.gp = gp;
        this.speed = kmhSpeed;
        this.pixelSpeed = (kmhSpeed * 1000.0 / 3600.0) * (gp.tileSize / 2.0) / gp.FPS;
        initCar(direction);
        getPlayerImage();
    }
    public void setSpriteNum(int num) {
        this.spriteNum = num;
    }
    public int getSpriteNum() {
        return spriteNum;
    }
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
    public boolean canMove() {
        return canMove;
    }

    public void getPlayerImage() {
        try {
            up = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/cars/car_up.png")));
            down = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/cars/car_down.png")));
            right = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/cars/car_right.png")));
            left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/cars/car_left.png")));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void move() {
        if (!canMove) return;

        switch (getSpriteNum()){
            case 1:
                worldY -= pixelSpeed;
                break;
            case 2:
                worldY += pixelSpeed;
                break;
            case 3:
                worldX += pixelSpeed;
                break;
            case 4:
                worldX -= pixelSpeed;
                break;
        }
        checkJunctionBoundary();
    }

    private void checkJunctionBoundary() {
        if (isPastJunction()) {
            this.canMove = true;
        }
    }
    private boolean isPastJunction() {
        int tileX = getCarTileX();
        int tileY = getCarTileY();

        switch(getSpriteNum()) {
            case 1: return tileY < JUNCTION_MIN_Y; // Up
            case 2: return tileY > JUNCTION_MAX_Y; // Down
            case 3: return tileX > JUNCTION_MAX_X; // Right
            case 4: return tileX < JUNCTION_MIN_X; // Left
            default: return true;
        }
    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (getSpriteNum()){
            case 1:
                image = up;
                break;
            case 2:
                image = down;
                break;
            case 3:
                image = right;
                break;
            case 4:
                image = left;
                break;
        }
        g2.drawImage(image, worldX, worldY, gp.tileSize, gp.tileSize, null);
    }
    public void initCar(String direction) {
        switch (direction){
            case "right":
                worldY = gp.tileSize * 5;
                worldX = 0;
                setSpriteNum(3);
                break;
            case "down":
                worldY = 0;
                worldX = 7 * gp.tileSize;
                setSpriteNum(2);
                break;
            case "up":
                worldX = 8 * gp.tileSize;
                worldY = gp.screenHeight - gp.tileSize;
                setSpriteNum(1);
                break;
            case "left":
                worldX = gp.screenWidth - gp.tileSize;
                worldY = gp.tileSize * 4;
                setSpriteNum(4);
                break;
        }
    }
    public int getCarTileX() {
        return worldX / gp.tileSize;
    }
    public int getCarTileY() {
        return worldY / gp.tileSize;
    }



}
