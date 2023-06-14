package org.example.cells;

import org.example.Point;
import org.example.iterable.DrivingPathChances;
import org.example.moving.BoardDirection;

import java.util.*;

public class Road extends Point implements Drivable {
//    private List<BoardDirection> availableOvertakingDirections = new ArrayList<>();

    private DrivingPathChances chances = new DrivingPathChances();

    private Lights lightsController = null;
    public Road(){
        super(CellType.ROAD);
    }

    @Override
    public boolean canDriveThrough() {
        if(lightsController == null){
            return true;
        }

        return lightsController.getLightColor() == LightColor.GREEN;
    }

    @Override
    public void setLightsController(Lights l) {
        lightsController = l;
    }

    @Override
    public BoardDirection getDriveDirection(BoardDirection from) {
        Random rand = new Random();
        int percent = rand.nextInt(100);
        for (BoardDirection to: BoardDirection.values()) {
            int chance = getDrivingPathChances().getChancesFromTo(from, to);
            if (percent < chance) {
                System.out.println("Returned: "+to);
                return to;
            }
            percent -= chance;
        }
        return null; // no available direction or error
    }

    @Override
    public DrivingPathChances getDrivingPathChances() {
        return chances;
    }

    @Override
    public void setDrivingPathChances(DrivingPathChances chances) {
        this.chances = new DrivingPathChances(chances);
    }

}
