package org.example.cells;

import org.example.Point;
import org.example.iterable.DrivingPathChances;
import org.example.moving.BoardDirection;

import java.util.List;
import java.util.Random;

public class Crossing extends Point implements Drivable, Walkable{
    private DrivingPathChances chances = new DrivingPathChances();
//    private int[] directionChances = new int[BoardDirection.values().length];
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

    @Override
    public BoardDirection getDriveDirection(BoardDirection from) {
        List<Integer> directionChances = chances.getChancesFrom(from).values().stream().toList();
        Random rand = new Random();
        int percent = rand.nextInt(100);
        int index = 0;
        for (int chance: directionChances) {
            if (percent < chance) {
                return BoardDirection.values()[index];
            }
            percent -= chance;
            ++index;
        }
        return null; // no available direction or error
    }

    @Override
    public DrivingPathChances getDrivingPathChances() {
        return chances;
    }

    @Override
    public void setDrivingPathChances(DrivingPathChances chances) {
        this.chances = chances;
    }
}
