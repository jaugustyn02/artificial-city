package org.example.cells;

import org.example.Point;
import org.example.moving.Direction;

import java.util.ArrayList;
import java.util.List;

public class Road extends Point implements Drivable {
    List<Direction>  availableDirections = new ArrayList<>();
    public Road(){
        super(CellType.ROAD);
        availableDirections.add(Direction.FORWARD);
    }

}
