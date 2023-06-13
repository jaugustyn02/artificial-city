package org.example.iterable;

import org.example.Board;
import org.example.MovingObject;
import org.example.cells.CellType;
import org.example.cells.Exit;
import org.example.cells.Road;
import org.example.helpers.Vector2D;
import org.example.moving.BoardDirection;
import org.example.moving.Car;

import java.util.ArrayList;
import java.util.List;

public class CarExit extends IterablePoint implements Exit {
    private final static BoardDirection[] directions = {
            BoardDirection.TOP, BoardDirection.RIGHT, BoardDirection.BOTTOM, BoardDirection.LEFT
    };
    private List<BoardDirection> neighbourRoadsDirections;

    public CarExit(){
        super(CellType.CAR_EXIT);
    }

    @Override
    public void iterate() {
        acquire();
    }

    @Override
    public void acquire() {
        if (neighbourRoadsDirections == null)
            addNeighbourRoads();
        for (BoardDirection direction : neighbourRoadsDirections){
            Vector2D vector = new Vector2D(x ,y);
            vector = vector.add(direction.getVector());
            MovingObject obj = board.getMovingObjectAt(vector.x(), vector.y());
            if (obj instanceof Car)
                board.removeMovingObjectsAt(vector.x(), vector.y());
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

    public void removeCar(Vector2D vector){
        board.removeMovingObjectsAt(vector.x(), vector.y());
    }
}
