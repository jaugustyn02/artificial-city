package org.example.iterable;

import org.example.cells.CellType;
import org.example.cells.Entrance;
import org.example.cells.WalkablePoint;
import org.example.helpers.Vector2D;
import org.example.moving.BoardDirection;

import java.util.ArrayList;
import java.util.List;

public class PedestrianEntrance extends IterablePoint implements Entrance {
    private final static BoardDirection[] directions = {
            BoardDirection.TOP, BoardDirection.RIGHT, BoardDirection.BOTTOM, BoardDirection.LEFT
    };
    private List<BoardDirection> neighbourWalkablePointDirections;
    private double pedestrianSpawnChance = 0.0;
    public PedestrianEntrance(){
        super(CellType.PEDESTRIAN_ENTRANCE);
    }

    @Override
    public void setSpawnChance(double spawnChance) {
        pedestrianSpawnChance = spawnChance;
    }

    @Override
    public void release() {
        for (BoardDirection direction : neighbourWalkablePointDirections){
            board.editDirection = direction;
            board.editType = CellType.PEDESTRIAN;
            Vector2D vector = new Vector2D(x ,y);
            vector = vector.add(direction.getVector());
            board.setCell(vector.x(), vector.y());
        }
    }

    @Override
    public void iterate() {
        if (neighbourWalkablePointDirections == null)
            addNeighbourWalkablePointDirections();
        double chanceLeft = pedestrianSpawnChance;
        while (chanceLeft > 0){
            double rand = Math.random();
            if (rand < chanceLeft){
                release();
            }
            chanceLeft -= rand;
        }
    }

    private void addNeighbourWalkablePointDirections(){
        if (board == null) return;
        neighbourWalkablePointDirections = new ArrayList<>();
        Vector2D position = new Vector2D(this.x, this.y);
        for (BoardDirection direction: directions){
            Vector2D vector = position.add(direction.getVector());
            if (inBounds(vector) && board.getPointAt(vector.x(), vector.y()) instanceof WalkablePoint){
                neighbourWalkablePointDirections.add(direction);
            }
        }
    }

    private boolean inBounds(Vector2D vector){
        if (board == null) return false;
        int x = vector.x();
        int y = vector.y();
        return (x >= 0 && y >= 0 && x < board.getWidth() && y < board.getHeight());
    }
}
