package org.example.cells;

import org.example.Config;
import org.example.iterable.DrivingPathChances;
import org.example.moving.BoardDirection;

import java.util.*;

public class Crossing extends WalkablePoint implements Drivable{
    private DrivingPathChances chances = new DrivingPathChances();
    private Lights lightsController = null;
    private int speedLimit = Config.maxVelocity;
    public Crossing(){
        super(CellType.CROSSING);
    }

    @Override
    public void setSpeedLimit(int limit) {
        speedLimit = limit;
    }

    @Override
    public int getSpeedLimit() {
        return speedLimit;
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
//        return null; // no available direction or error
        return from;
    }

    @Override
    public DrivingPathChances getDrivingPathChances() {
        return chances;
    }

    @Override
    public void setDrivingPathChances(DrivingPathChances chances) {
        this.chances = chances;
    }

    @Override
    public String getInfo(){
        return super.getInfo() + ", path chances: "+chances + ", speed limit: " + speedLimit;
    }
}
