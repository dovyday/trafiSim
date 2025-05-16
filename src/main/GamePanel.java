package main;

import entity.Car;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    public final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int FPS = 60;

    Thread gameThread;
    TileManager tileM = new TileManager(this);
    List<Car> cars = new ArrayList<>();


    public GamePanel() {
        this.setPreferredSize((new Dimension(screenWidth, screenHeight)));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        initCars();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void paintComponent (Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);
        for (Car car : cars) {
            car.draw(g2);
        }

        g2.dispose();

    }
    public void update() {
        for (Car car : cars) {
            car.move();
        }
    }
    public void initCars(){
        cars.add(new Car(this, 30, "right"));
        cars.add(new Car(this, 30, "left"));
        cars.add(new Car(this, 30, "down"));
        cars.add(new Car(this, 30, "up"));
    }
}

