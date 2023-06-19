package org.example.moving;

import org.example.MovingObject;
import org.example.Point;
import org.example.cells.CellType;
import org.example.helpers.Vector2D;
import org.example.iterable.PedestrianExit;

public class Pedestrian extends MovingObject {
    private Vector2D nextPosition;
    private PedestrianExit targetExit;

    public Pedestrian() {
        super(CellType.PEDESTRIAN);
    }

    public void setTargetExit(PedestrianExit exit){
        targetExit = exit;
    }

    @Override
    public void iterate(Point[][] points, MovingObject[][] movingObjects) {

    }

    @Override
    public void move() {
        this.x = nextPosition.x();
        this.y = nextPosition.y();
    }
}
