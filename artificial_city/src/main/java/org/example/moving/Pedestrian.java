package org.example.moving;

import org.example.MovingObject;
import org.example.Point;
import org.example.cells.CellType;

public class Pedestrian extends MovingObject {
    boolean blocked = false;

    public Pedestrian() {
        super(CellType.PEDESTRIAN);
    }

    @Override
    public void iterate(Point[][] points, MovingObject[][] movingObjects) {

    }

    @Override
    public void move() {

    }
}