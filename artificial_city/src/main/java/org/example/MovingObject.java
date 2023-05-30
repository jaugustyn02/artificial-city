package org.example;

import org.example.cells.CellType;
import org.example.cells.Lights;
import org.example.moving.BoardDirection;


public abstract class MovingObject {
    protected int x;
    protected int y;
    protected int velocity = 0;
    protected CellType type;
    protected BoardDirection direction;

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

    public void setVelocity(int velocity){
        this.velocity = velocity;
    }
}
