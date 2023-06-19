package org.example.cells;

import org.example.Point;

import java.awt.*;

public class Lights extends Point implements Walkable{
    Point pointControlled;
    public int x;
    public int y;
    private LightColor currentState;
    public Lights(int x, int y){
        super(CellType.LIGHTS);
        this.x = x;
        this.y = y;
        currentState = LightColor.RED;
    }
    public Color getColor(){
        return currentState.getColor();
    }

    public void setPoint(Point point) {
        pointControlled = point;
    }

    public LightColor  getLightColor() {
        return currentState;
    }

    public void setColor(LightColor c) {
        currentState = c;
    }

    @Override
    public String getInfo(){
        return super.getInfo() + ", state: "+currentState;
    }
}
