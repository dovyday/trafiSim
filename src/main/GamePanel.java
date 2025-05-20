package main;

import entity.Car;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable, KeyListener {

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
    public List<Car> cars = new ArrayList<>();
    private JunctionChecker junctionChecker;

    GameState gameState = GameState.MENU;

    private volatile boolean isLoading = false;

    public GamePanel() {
        this.setPreferredSize((new Dimension(screenWidth, screenHeight)));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(this);
        initCars();
        junctionChecker = new JunctionChecker(this);
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
                if (gameState == GameState.PLAY) {
                    update();
                }
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



        switch (gameState) {
            case MENU:
                drawMenu(g2);
                break;
            case PLAY:
                tileM.draw(g2);
                for (Car car : cars) {
                    car.draw(g2);
                }
                break;
            case PAUSE:
                tileM.draw(g2);
                for (Car car : cars) {
                    car.draw(g2);
                }
                drawPauseScreen(g2);
                break;
        }

        g2.dispose();

    }
    public void update() {
        if (isLoading) return;

        junctionChecker.updateJunction();

        for (Car car : cars) {
            car.move();

        }
    }
    public void initCars(){
        cars.add(new Car(this, 26, "right"));
//        cars.add(new Car(this, 12, "left"));
//        cars.add(new Car(this, 11, "down"));
        cars.add(new Car(this, 10, "up"));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (gameState) {
            case MENU:
                if (code == KeyEvent.VK_ENTER) {
                    resetCarPositions();
                    gameState = GameState.PLAY;
                } else if (code == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else if (code == KeyEvent.VK_L) {
                    loadGameState();
                    gameState = GameState.PLAY;
                }

                break;
            case PLAY:
                if (code == KeyEvent.VK_ESCAPE) {
                    gameState = GameState.PAUSE;
                }
                break;
            case PAUSE:
                if (code == KeyEvent.VK_ESCAPE) {
                    gameState = GameState.PLAY;
                } else if (code == KeyEvent.VK_S) {
                    saveGameState();
                    System.exit(0);
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void drawMenu(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);


        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        String text = "Press ENTER to start";
        int x = getXForCenteredText(text, g2);
        g2.drawString(text, x, 100);


        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        text = "Press L to load";
        x = getXForCenteredText(text, g2);
        g2.drawString(text, x, 200);

        text = "Press ESC to exit";
        x = getXForCenteredText(text, g2);
        g2.drawString(text, x, 250);
    }

    private void drawPauseScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);


        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        String text = "PAUSED";
        int x = getXForCenteredText(text, g2);
        g2.drawString(text, x, 200);


        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        text = "Press ESC to resume";
        x = getXForCenteredText(text, g2);
        g2.drawString(text, x, 250);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        text = "Press S to save";
        x = getXForCenteredText(text, g2);
        g2.drawString(text, x, 300);
    }

    private int getXForCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return screenWidth / 2 - length / 2;
    }

    public void saveGameState() {
        Thread saveThread = new Thread(() -> {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("game_state.dat"))) {
                List<Car> carsToSave = new ArrayList<>(cars);
                oos.writeObject(carsToSave);
                SwingUtilities.invokeLater(() ->
                    System.out.println("Žaidimo būsena sėkmingai išsaugota"));
            } catch (IOException e) {
                SwingUtilities.invokeLater(() ->
                    System.err.println("Klaida išsaugant: " + e.getMessage()));
            }
        });
        saveThread.start();
    }

    public void loadGameState() {
        isLoading = true;
        Thread loadThread = new Thread(() -> {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("game_state.dat"))) {
                List<Car> loadedCars = (List<Car>) ois.readObject();
                SwingUtilities.invokeLater(() -> {
                    cars = loadedCars;
                    for (Car car : cars) {
                        car.gp = this;
                        car.setCanMove(true);
                        car.getPlayerImage();
                    }
                    isLoading = false;
                    System.out.println("Žaidimo būsena atkurta.");
                });

            } catch (IOException | ClassNotFoundException e) {
                isLoading = false;
            }
        });
        loadThread.start();
    }

    public void resetCarPositions() {
        cars.clear();
        initCars();
    }

}


