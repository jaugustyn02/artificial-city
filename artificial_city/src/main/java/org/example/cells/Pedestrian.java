package org.example.cells;

import org.example.Point;

public class Pedestrian extends Point {
    boolean blocked = false;

    public Pedestrian() {
        super(CellType.PEDESTRIAN);
    }
}
