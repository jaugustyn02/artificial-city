package org.example.cells;

import org.example.Point;

public class Crossing extends Point implements Drivable, Walkable{
    public Crossing(){
        super(CellType.CROSSING);
    }
}
