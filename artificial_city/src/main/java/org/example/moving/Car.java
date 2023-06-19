package org.example.moving;

import org.example.Config;
import org.example.MovingObject;
import org.example.Point;
import org.example.cells.CellType;
import org.example.cells.Drivable;
import org.example.helpers.Vector2D;
import org.example.iterable.CarExit;



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
        accelerate(points);
        slowDown(points,movingObjects);
        randomSlowDown();
    }

    public void accelerate(Point[][] points) {
        if(points[x][y] instanceof Drivable d){
            int speedLimit = d.getSpeedLimit();
            if (velocity < speedLimit){
                velocity++;
            } else if(velocity > speedLimit){
                velocity = speedLimit;
            }
        } else {
            if (velocity < Config.maxVelocity){
                velocity++;
            }
        }
    }

    public void slowDown(Point[][] points,MovingObject[][] movingObjects ) {

        BoardDirection driveDirection = this.direction;
        int distanceDriven = 0;
        Vector2D pos = new Vector2D(x,y);
        int maxVelocity = velocity;
        int maxDistance = Config.maxVelocity;

        boolean changedDirection = false;

        // na koniec


        while (distanceDriven < velocity){
            pos = pos.add(direction.getVector());
            MovingObject obj = movingObjects[pos.x()][pos.y()];
            Point point = points[pos.x()][pos.y()];
            maxDistance--;



            if(obj != null || !(point instanceof Drivable d) || !d.canDriveThrough() || maxDistance == 0){
                if(point instanceof CarExit exit){
                    exit.removeCar(new Vector2D(x,y));
                }
                maxVelocity = distanceDriven;

                break;
            }
            int speedLimit = d.getSpeedLimit();
            if(velocity > speedLimit){
                if(maxDistance > speedLimit){
                    maxDistance = speedLimit;
                }
            }

            distanceDriven++;

            driveDirection = d.getDriveDirection(this.direction);
            if (driveDirection == null){
                driveDirection = this.direction;
            }
            if (driveDirection != this.direction){
                changedDirection = true;
                maxVelocity = distanceDriven;
                break;
            }
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

    public void changeLane(Point newPoint){}
    public void move(){
        x = nextPosition.x();
        y = nextPosition.y();
    }
}
