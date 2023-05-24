package org.example.cells;

import org.example.Point;

public class Sidewalk extends Point implements Walkable{
    public Sidewalk(){
        super(CellType.SIDEWALK);
    }
}
