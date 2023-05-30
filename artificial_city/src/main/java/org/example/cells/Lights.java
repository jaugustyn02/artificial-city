package org.example.cells;

import org.example.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Lights extends Point implements Walkable{
    List<Point> pointsControlled = new ArrayList<>();
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

    public void addPoint(Point point) {
        pointsControlled.add(point);
    }

    public LightColor  getLightColor() {
        return currentState;
    }

    public void setColor(LightColor c) {
        currentState = c;
    }
}
