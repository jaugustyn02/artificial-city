package org.example.moving;

import org.example.Config;
import org.example.MovingObject;
import org.example.Point;
import org.example.cells.CellType;
import org.example.cells.Drivable;
import org.example.helpers.Vector2D;


public class Car extends MovingObject{
    public final static double slowDownChance = 0.2;

    private Vector2D nextPosition;

    public Car() {
        this(BoardDirection.RIGHT);
    }

    public Car(BoardDirection direction){
        super(CellType.CAR);
        this.direction = direction;
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

        int distanceToNextCar = 0;
        Vector2D pos = new Vector2D(x,y);
        int maxVelocity = velocity;
        while (distanceToNextCar < velocity){
            pos = pos.add(direction.getVector());
            MovingObject obj = movingObjects[pos.x()][pos.y()];
            Point point = points[pos.x()][pos.y()];

            if(obj != null || !(point instanceof Drivable d) || !d.canDriveThrough()){
                maxVelocity = distanceToNextCar;
                break;
            }

            distanceToNextCar++;
        }
        Vector2D toAdd = direction.getVector().multiply(maxVelocity);
        nextPosition = new Vector2D(x,y).add(toAdd);
        velocity = maxVelocity;

    }

    public void randomSlowDown() {
        if (Math.random() <= slowDownChance && velocity > 0){
            velocity--;
        }
    }

    public void changeLane(Point newPoint){

    }
    public void move(){
        x = nextPosition.x();
        y = nextPosition.y();
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
