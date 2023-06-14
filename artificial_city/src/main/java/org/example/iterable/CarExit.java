package org.example.iterable;

import org.example.cells.CellType;
import org.example.cells.Exit;
import org.example.helpers.Vector2D;

public class CarExit extends IterablePoint implements Exit {
    public CarExit(){
        super(CellType.CAR_EXIT);
    }

    @Override
    public void iterate() {}

    @Override
    public void acquire() {}

    public void removeCar(Vector2D vector){
        board.removeMovingObjectsAt(vector.x(), vector.y());
    }
}
