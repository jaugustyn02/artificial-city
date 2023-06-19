package org.example.cells;

import org.example.iterable.DrivingPathChances;
import org.example.moving.BoardDirection;


public interface Drivable {
//    Lights lightsController = null;
    void setLightsController(Lights l);

    boolean canDriveThrough();

    BoardDirection getDriveDirection(BoardDirection from);

    DrivingPathChances getDrivingPathChances();
    void setDrivingPathChances(DrivingPathChances chances);
    int getSpeedLimit();
    void setSpeedLimit(int limit);
}
