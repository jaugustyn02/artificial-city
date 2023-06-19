package org.example.iterable;

import org.example.Board;
import org.example.cells.CellType;
import org.example.cells.Exit;
import org.example.cells.WalkablePoint;


public class PedestrianExit extends WalkablePoint implements Exit {
    private Board board;
    public int EXIT_ID;

    public void setBoard(Board board){
        this.board = board;
    }

    public PedestrianExit(){
        super(CellType.PEDESTRIAN_EXIT);
        exitStaticFields.put(this, 0.0);
    }

    @Override
    public void acquire() {
        board.removeMovingObjectsAt(getPosition().x(), getPosition().y());
        resetNumOfPedestrians();
    }

    @Override
    public String getInfo(){
        return super.getInfo() + ", ID: " + EXIT_ID;
    }
}
