package org.example.cells;

import org.example.moving.BoardDirection;
import org.example.moving.Direction;

import java.util.List;

public interface Drivable {
//    Lights lightsController = null;
    void setLightsController(Lights l);

    boolean canDriveThrough();

    public List<BoardDirection> getAvailableDirections();

    public BoardDirection getDriveDirection();

    public void setDirectionChances(int[] chances);

    public int[] getDrivableChances();
}
