package org.example;

import org.example.cells.CellType;
import org.example.moving.BoardDirection;


public abstract class MovingObject extends Point {
    protected int x;
    protected int y;
    protected int velocity = 0;
    protected BoardDirection direction;

    public MovingObject(CellType type){
        super(type);
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

    @Override
    public String getInfo(){
        return super.getInfo() + ", direction: "+direction+", velocity: "+velocity;
    }
}
