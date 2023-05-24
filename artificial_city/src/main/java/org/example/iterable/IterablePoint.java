package org.example.iterable;

import org.example.Board;
import org.example.Point;
import org.example.cells.CellType;

public abstract class IterablePoint extends Point {
    protected int x;
    protected int y;
    protected Board board;

    public IterablePoint(CellType type){
        super(type);
    }

    public abstract void iterate();
    public void setBoard(Board board){
        this.board = board;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
