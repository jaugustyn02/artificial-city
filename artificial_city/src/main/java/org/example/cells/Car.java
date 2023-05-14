package org.example.cells;

import org.example.Config;
import org.example.Point;

public class Car extends Point implements Moving {
    private int velocity = 0;
    public final static double slowDownChance = 0.5;


    public Car() {
        super(CellType.CAR);
    }

    @Override
    public void iterate() {

    }

    public void move(Point newPoint) {

    }

    public void accelerate() {
        if (velocity < Config.maxVelocity){
            velocity++;
        }
    }

    public void slowDown(int distanceToNextCar) {
        if(distanceToNextCar < velocity){
            velocity = distanceToNextCar;
        }
    }

    public void randomSlowDown() {
        if (Math.random() <= slowDownChance && velocity > 0){
            velocity--;
        }
    }
    public void changeLane(Point newPoint){
    }

//    public void moveCar(Point newPoint, boolean exceededPeriodicBoundaries){
//        if (numOfMovesOnNewLane == numOfMovesBeforeColorReset){
//            currentColorID = defaultColorID;
//        }
//        if (!hasMoved && velocity > 0) {
//            if (!exceededPeriodicBoundaries || Math.random() > disappearChance) {
//                newPoint.velocity = velocity;
//                newPoint.hasMoved = true;
//                newPoint.currentColorID = currentColorID;
//                newPoint.numOfMovesOnNewLane = numOfMovesOnNewLane + 1;
//            }
//            clear();
//        }
//    }
}
