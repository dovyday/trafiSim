package entity;

import java.awt.image.BufferedImage;

public class Entity {
    public int worldX, worldY;
    public double speed;
    public double pixelSpeed;

    public BufferedImage up, down, left, right;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum;
}
