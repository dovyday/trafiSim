package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Car extends Entity{
    GamePanel gp;

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

    public void getPlayerImage() {
        try {
            up = ImageIO.read(new File("C:\\Users\\dove\\IdeaProjects\\trafi\\.idea\\res\\cars\\car_up.png"));
            down = ImageIO.read(new File("C:\\Users\\dove\\IdeaProjects\\trafi\\.idea\\res\\cars\\car_down.png"));
            right = ImageIO.read(new File("C:\\Users\\dove\\IdeaProjects\\trafi\\.idea\\res\\cars\\car_right.png"));
            left = ImageIO.read(new File("C:\\Users\\dove\\IdeaProjects\\trafi\\.idea\\res\\cars\\car_left.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void move() {
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

}
