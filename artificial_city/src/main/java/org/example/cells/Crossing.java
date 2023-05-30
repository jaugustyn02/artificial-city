package org.example.cells;

import org.example.Point;

public class Crossing extends Point implements Drivable, Walkable{
    private Lights lightsController = null;
    public Crossing(){
        super(CellType.CROSSING);
    }

    @Override
    public void setLightsController(Lights l) {
        lightsController = l;
    }

    @Override
    public boolean canDriveThrough() {
        return true;
    }
}
