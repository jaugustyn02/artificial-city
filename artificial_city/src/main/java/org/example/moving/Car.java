package org.example.moving;

import org.example.Config;
import org.example.MovingObject;
import org.example.Point;
import org.example.cells.CellType;
import org.example.cells.Drivable;
import org.example.helpers.Vector2D;
import org.example.iterable.CarExit;

import java.util.List;


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

        BoardDirection driveDirection = this.direction;
        int distanceToNextCar = 0;
        Vector2D pos = new Vector2D(x,y);
        int maxVelocity = velocity;

        boolean changedDirection = false;

        while (distanceToNextCar < velocity){
            pos = pos.add(direction.getVector());
            MovingObject obj = movingObjects[pos.x()][pos.y()];
            Point point = points[pos.x()][pos.y()];

            if(obj != null || !(point instanceof Drivable d) || !d.canDriveThrough()){
                if(point instanceof CarExit exit){
                    exit.removeCar(new Vector2D(x,y));
                }
                maxVelocity = distanceToNextCar;

                break;
            }
            distanceToNextCar++;

            driveDirection = d.getDriveDirection();
            if (driveDirection == null){
                driveDirection = this.direction;
            }
            if (driveDirection != this.direction){
                changedDirection = true;
                maxVelocity = distanceToNextCar;
                break;
            }

//            List<BoardDirection> avDir = d.getAvailableDirections();
//            if (avDir.size() == 1){
//                continue;
//            }
//            if(avDir.size() == 0){
//                maxVelocity = distanceToNextCar;
//                break;
//            }
//
//            int index = (int)(Math.random()*avDir.size());
//            BoardDirection newDirection = avDir.get(index);
//            if(newDirection == direction){
//                continue;
//            }

//            this.direction = newDirection;
//            changedDirection = true;
//            maxVelocity = distanceToNextCar;
//            break;


        }
        Vector2D toAdd = direction.getVector().multiply(maxVelocity);
        nextPosition = new Vector2D(x,y).add(toAdd);
        velocity = maxVelocity;

        if(changedDirection){
            velocity = 0;
            this.direction = driveDirection;
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
        x = nextPosition.x();
        y = nextPosition.y();
    }
}
