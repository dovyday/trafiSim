package entity;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Entity implements Serializable {
    public int worldX, worldY;
    public double speed;
    public double pixelSpeed;

    public transient BufferedImage up, down, left, right;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum;
}
