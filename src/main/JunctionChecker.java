package main;

import entity.Car;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class JunctionChecker {
    GamePanel gp;
    Queue<Car> waitingCars = new LinkedList<>();


    public static final int JUNCTION_MIN_X = 7;
    public static final int JUNCTION_MAX_X = 9;
    public static final int JUNCTION_MIN_Y = 4;
    public static final int JUNCTION_MAX_Y = 6;

    public JunctionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public boolean isCarInJunction(Car car) {
        int tileX = car.getCarTileX();
        int tileY = car.getCarTileY();
        return tileX >= JUNCTION_MIN_X && tileX <= JUNCTION_MAX_X &&
                tileY >= JUNCTION_MIN_Y && tileY <= JUNCTION_MAX_Y;
    }

    public boolean isJunctionEmpty() {
        for (Car car : gp.cars) {
            if (isCarInJunction(car)) {
                return false;
            }
        }
        return true;
    }
    public void checkApproachingCars() {
        for (Car car : gp.cars) {
            if (isAboutToEnterJunction(car) && !waitingCars.contains(car)) {
                waitingCars.add(car);
            }
        }
    }
    private boolean isAboutToEnterJunction(Car car) {
        int tileX = car.getCarTileX();
        int tileY = car.getCarTileY();

        switch(car.getSpriteNum()) {
            case 1: // up
                return tileX >= JUNCTION_MIN_X && tileX <= JUNCTION_MAX_X &&
                        tileY == JUNCTION_MAX_Y + 1;
            case 2: // down
                return tileX >= JUNCTION_MIN_X && tileX <= JUNCTION_MAX_X &&
                        tileY == JUNCTION_MIN_Y - 1;
            case 3: // right
                return tileY >= JUNCTION_MIN_Y && tileY <= JUNCTION_MAX_Y &&
                        tileX == JUNCTION_MIN_X - 1;
            case 4: // left
                return tileY >= JUNCTION_MIN_Y && tileY <= JUNCTION_MAX_Y &&
                        tileX == JUNCTION_MAX_X + 1;
            default:
                return false;
        }
    }

    public void updateJunction() {
        waitingCars.clear();

       for (Car car : gp.cars) {
           if (isCarInJunction(car)) {
               car.setCanMove(true);

               checkApproachingCars();
               for (Car waitingCar : waitingCars) {
                   waitingCar.setCanMove(false);
               }
               return;
           }
           if (isAboutToEnterJunction(car)) {
               waitingCars.add(car);
               car.setCanMove(false);
           }
       }
       if (isJunctionEmpty() && !waitingCars.isEmpty()) {
           Car nextCar = determinePriorityCar();
           nextCar.setCanMove(true);
           waitingCars.remove(nextCar);
       }
    }
    private Car determinePriorityCar() {
        if (waitingCars.isEmpty()) return null;


        List<Car> carList = new ArrayList<>(waitingCars);

        carList.sort((c1, c2) -> {
            return Integer.compare(getDirectionPriority(c2), getDirectionPriority(c1));
        });

        return carList.get(0);
    }
    private int getDirectionPriority(Car car) {
        switch(car.getSpriteNum()) {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 2;
            default:
                return 0;
        }
    }
}
