package org.example.cells;

import org.example.Config;
import org.example.iterable.DrivingPathChances;
import org.example.moving.BoardDirection;

import java.util.*;

public class Crossing extends WalkablePoint implements Drivable {
    private DrivingPathChances chances = new DrivingPathChances();
    private Lights lightsController = null;
    private int speedLimit = Config.maxVelocity;
    protected int numOfPedestriansOnCrossingMutex = 0;


    public Crossing() {
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
        return numOfPedestriansOnCrossingMutex == 0;
    }

    @Override
    public BoardDirection getDriveDirection(BoardDirection from) {
        Random rand = new Random();
        int percent = rand.nextInt(100);
        for (BoardDirection to: BoardDirection.values()) {
            int chance = getDrivingPathChances().getChancesFromTo(from, to);
            if (percent < chance) {
                return to;
            }
            percent -= chance;
        }
        return from;
    }

    @Override
    public DrivingPathChances getDrivingPathChances() {
        return chances;
    }

    @Override
    public void setDrivingPathChances(DrivingPathChances chances) {
        this.chances = new DrivingPathChances(chances);
    }

    @Override
    public String getInfo() {
        return super.getInfo() + ", path chances: " + chances + ", speed limit: " + speedLimit + ", mutex: " + numOfPedestriansOnCrossingMutex;
    }

    // -------------------------canDriveThrough------------------------------

    public void blockNeighbourCrossing() {
        ++this.numOfPedestriansOnCrossingMutex;
        for (WalkablePoint neighbour : walkableNeighbours) {
            if (neighbour instanceof Crossing crossing
                    && crossing.numOfPedestriansOnCrossingMutex < this.numOfPedestriansOnCrossingMutex) {
                crossing.blockNeighbourCrossing();
            }
        }
    }

    public void unblockNeighbourCrossing() {
        --numOfPedestriansOnCrossingMutex;
        for (WalkablePoint neighbour : walkableNeighbours) {
            if (neighbour instanceof Crossing crossing
                    && crossing.numOfPedestriansOnCrossingMutex > this.numOfPedestriansOnCrossingMutex) {
                crossing.unblockNeighbourCrossing();
            }
        }
    }
}
