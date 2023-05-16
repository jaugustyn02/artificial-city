package org.example;

import org.example.cells.CellType;

import java.util.Vector;

public abstract class MovingObject {
    protected int velocity;
    protected int x;
    protected int y;
    protected CellType type;
    public MovingObject(CellType type){
        this.type = type;
    }
    public abstract void iterate(Point[][] points, MovingObject[][] movingObjects);
    public abstract void move();
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
