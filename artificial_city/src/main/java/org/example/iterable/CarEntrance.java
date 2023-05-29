package org.example.iterable;

import org.example.Config;
import org.example.cells.CellType;
import org.example.cells.Entrance;
import org.example.cells.Road;
import org.example.helpers.Vector2D;
import org.example.moving.BoardDirection;

import java.util.*;

public class CarEntrance extends IterablePoint implements Entrance {
    private final static BoardDirection[] directions = {
            BoardDirection.TOP, BoardDirection.RIGHT, BoardDirection.BOTTOM, BoardDirection.LEFT
    };
    private double carAppearanceChance = 0.1;
    private List<BoardDirection> neighbourRoadsDirections;

    public CarEntrance(){
        super(CellType.CAR_ENTRANCE);
    }

    @Override
    public void iterate() {
        release();
    }

    @Override
    public void release(){
        if (neighbourRoadsDirections == null)
            addNeighbourRoads();
        if (Math.random() < carAppearanceChance){
            for (BoardDirection direction : neighbourRoadsDirections){
                board.editDirection = direction;
                board.editType = CellType.CAR;
                Vector2D vector = new Vector2D(x ,y);
                vector = vector.add(direction.getVector());
                board.setCell(vector.x(), vector.y());
                board.getMovingObjectAt(vector.x(), vector.y()).setVelocity(Config.maxVelocity);
            }
        }
    }

    private void addNeighbourRoads(){
        if (board == null) return;
        neighbourRoadsDirections = new ArrayList<>();
        Vector2D position = new Vector2D(this.x, this.y);
        for (BoardDirection direction: directions){
            Vector2D vector = position.add(direction.getVector());
            if (inBounds(vector) && board.getPointAt(vector.x(), vector.y()) instanceof Road){
                neighbourRoadsDirections.add(direction);
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
