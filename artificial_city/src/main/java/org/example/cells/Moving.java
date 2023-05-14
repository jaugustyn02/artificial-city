package org.example.cells;

import org.example.Point;

public interface Moving {
    void move(Point newPoint);
    void accelerate();
    void slowDown(int distanceToNextCar);
    void randomSlowDown();
    void changeLane(Point newPoint);
}
