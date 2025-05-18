package main;

import entity.Car;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class JunctionChecker {
    GamePanel gp;
    Queue<Car> waitingCars = new LinkedList<>();

    // Define junction boundaries
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
        // Check if car is one tile away from junction based on its direction
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

        // Simple right-hand rule implementation
        // Cars coming from the right have priority
        List<Car> carList = new ArrayList<>(waitingCars);

        // Sort based on direction (right-hand rule priority)
        carList.sort((c1, c2) -> {
            // Higher priority comes first
            return Integer.compare(getDirectionPriority(c2), getDirectionPriority(c1));
        });

        return carList.get(0);
    }
    private int getDirectionPriority(Car car) {
        // Assign priority based on right-hand rule
        // The car to the right has higher priority
        switch(car.getSpriteNum()) {
            case 1: // up - gives way to right (left-turning cars)
                return 1;
            case 2: // down - gives way to left (right-turning cars)
                return 3;
            case 3: // right - highest priority
                return 4;
            case 4: // left - lowest priority
                return 2;
            default:
                return 0;
        }
    }
}
