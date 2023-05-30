package org.example.cells;

import org.example.Point;
import org.example.moving.Direction;

import java.util.ArrayList;
import java.util.List;

public class Road extends Point implements Drivable {
    List<Direction>  availableDirections = new ArrayList<>();
    private Lights lightsController = null;
    public Road(){
        super(CellType.ROAD);
        availableDirections.add(Direction.FORWARD);
    }

    @Override
    public boolean canDriveThrough() {
        if(lightsController == null){
            return true;
        }

        if(lightsController.getLightColor() == LightColor.GREEN){
            return true;
        }
        return false;
    }

    @Override
    public void setLightsController(Lights l) {
        lightsController = l;
    }
}
