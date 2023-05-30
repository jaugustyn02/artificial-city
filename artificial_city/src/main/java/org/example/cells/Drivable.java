package org.example.cells;

import org.example.moving.Direction;

import java.util.List;

public interface Drivable {
//    Lights lightsController = null;
    void setLightsController(Lights l);

    boolean canDriveThrough();
}
