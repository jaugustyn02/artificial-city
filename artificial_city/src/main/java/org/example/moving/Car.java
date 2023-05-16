package org.example.moving;

import org.example.Config;
import org.example.MovingObject;
import org.example.Point;
import org.example.cells.CellType;
import org.example.cells.Drivable;
import org.example.cells.Road;
import org.example.helpers.Vector2D;

public class Car extends MovingObject{
    private int velocity = 0;
    private BoardDirection direction;
    public final static double slowDownChance = 0.2;

    private Vector2D tmpPosition;

    public Car() {
        super(CellType.CAR);
        direction = BoardDirection.RIGHT;
    }

    @Override
    public void iterate(Point[][] points, MovingObject[][] movingObjects) {
        // Nagel model
        accelerate();
        slowDown(points,movingObjects);
        randomSlowDown();
    }

    public void accelerate() {
        if (velocity < Config.maxVelocity){
            velocity++;
        }
    }

    public void slowDown(Point[][] points,MovingObject[][] movingObjects ) {
//        if(distanceToNextCar < velocity){
//            velocity = distanceToNextCar;
//        }
        int distanceToNextCar = 0;
        Vector2D pos = new Vector2D(x,y);
        while (distanceToNextCar < velocity){
            distanceToNextCar++;
            pos = pos.add(direction.getVector());

            MovingObject obj = movingObjects[pos.x()][pos.y()];

            if(obj != null){
                velocity = distanceToNextCar-1;
                break;
            }
        }

        Vector2D toAdd = direction.getVector().multiply(velocity);
        tmpPosition = toAdd.add(new Vector2D(x,y));

        while (!(points[tmpPosition.x()][tmpPosition.y()] instanceof Drivable)){
            tmpPosition = tmpPosition.add(direction.getVector().multiply(-1));
            velocity--;
        }


    }

    public void randomSlowDown() {
        if (Math.random() <= slowDownChance && velocity > 0){
            velocity--;
        }
    }

    public void changeLane(Point newPoint){

    }
    public void move(){
        x = tmpPosition.x();
        y = tmpPosition.y();
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
