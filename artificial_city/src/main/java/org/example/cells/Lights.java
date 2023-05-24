package org.example.cells;

import org.example.Point;

public class Lights extends Point implements Drivable, Walkable{
    public Lights(){
        super(CellType.LIGHTS);
    }
}
