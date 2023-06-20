package org.example.cells;

import org.example.Config;
import org.example.Point;
import org.example.helpers.Vector2D;
import org.example.iterable.PedestrianExit;

import java.util.*;

public abstract class WalkablePoint extends Point {
    private final List<WalkablePoint> walkableNeighbours = new ArrayList<>();
    protected final Map<WalkablePoint, Boolean> neighbourIsOnDiagonal = new HashMap<>();
    protected final Map<PedestrianExit, Double> exitStaticFields = new HashMap<>();
    private static final double STATIC_FIELD_MAX = 100000;
    private int numOfPedestrians = 0;
    private Vector2D position;

    public WalkablePoint(CellType type) {
        super(type);
    }

    public void addWalkableNeighbour(WalkablePoint neighbour) {
        walkableNeighbours.add(neighbour);
        neighbourIsOnDiagonal.put(neighbour, false);
    }

    public void setNeighbourIsOnDiagonal(WalkablePoint neighbour) {
        neighbourIsOnDiagonal.put(neighbour, true);
    }

    public boolean neighbourIsOnDiagonal(WalkablePoint neighbour){
        return neighbourIsOnDiagonal.get(neighbour);
    }

    public List<WalkablePoint> getWalkableNeighbours(){
        return walkableNeighbours;
    }

    public double getStaticField(PedestrianExit exit) {
        return exitStaticFields.getOrDefault(exit, STATIC_FIELD_MAX);
    }

    public void setStaticField(PedestrianExit exit, double staticField){
        exitStaticFields.put(exit, staticField);
    }

    public boolean calcStaticField(PedestrianExit exit) {
        double minStaticField = STATIC_FIELD_MAX;
        double field_increase = 1.0;

        if (!walkableNeighbours.isEmpty()){
            WalkablePoint minStaticFieldPoint = walkableNeighbours.stream().min(Comparator.comparingDouble(p -> p.getStaticField(exit))).get();

            if (neighbourIsOnDiagonal(minStaticFieldPoint))
                field_increase = Math.sqrt(2);

            minStaticField = minStaticFieldPoint.getStaticField(exit);
        }

        if (this.getStaticField(exit) > minStaticField + field_increase){
            exitStaticFields.put(exit, minStaticField + field_increase);
            return true;
        }
        return false;
    }

    public boolean isOccupied(){
        return numOfPedestrians >= Config.maxPedestriansAtPoint;
    }

    public void setPosition(Vector2D position){
        this.position = new Vector2D(position.x(), position.y());
    }

    public Vector2D getPosition(){
        return position;
    }

    public void incrementNumOfPedestrians(){
        ++numOfPedestrians;
    }

    public void decrementNumOfPedestrians(){
        --numOfPedestrians;
    }

    public void resetNumOfPedestrians(){
        numOfPedestrians = 0;
    }

    @Override
    public String getInfo(){
        return super.getInfo() + ", staticFields: " + exitStaticFields.values() + ", numOfPedestrians: " + numOfPedestrians;
    }
}
