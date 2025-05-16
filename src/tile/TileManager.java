package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenRow][gp.maxScreenCol];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/road_line_up.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/road_line_down.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(new File("C:\\Users\\dove\\IdeaProjects\\trafi\\.idea\\res\\tiles\\road_line_right.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(new File("C:\\Users\\dove\\IdeaProjects\\trafi\\.idea\\res\\tiles\\road_line_left.png"));

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(new File("C:\\Users\\dove\\IdeaProjects\\trafi\\.idea\\res\\tiles\\road.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap() {
        mapTileNum = new int[][] {
                {1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,1},
                {2,2,2,2,2,2,2,5,5,2,2,2,2,2,2,2},
                {0,0,0,0,0,0,0,5,5,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,1}
        };
    }



    public void draw(Graphics2D g2) {
        for(int row = 0; row < gp.maxScreenRow; row++) {
            for(int col = 0; col < gp.maxScreenCol; col++) {
                int tileNum = mapTileNum[row][col]; // Note: row first
                if(tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null) {
                    g2.drawImage(
                            tile[tileNum].image,
                            col * gp.tileSize,
                            row * gp.tileSize,
                            gp.tileSize,
                            gp.tileSize,
                            null
                    );
                }
            }
        }
    }
}
