package org.example.moving;

import org.example.MovingObject;
import org.example.Point;
import org.example.cells.CellType;
import org.example.cells.WalkablePoint;
import org.example.helpers.Vector2D;
import org.example.iterable.PedestrianExit;

import java.util.Comparator;
import java.util.Optional;

public class Pedestrian extends MovingObject {
    private Vector2D nextPosition;
    private PedestrianExit targetExit;

    public Pedestrian() {
        super(CellType.PEDESTRIAN);
    }

    public void setTargetExit(PedestrianExit exit){
        targetExit = exit;
    }

    @Override
    public void iterate(Point[][] points, MovingObject[][] movingObjects) {
        WalkablePoint currentPoint = (WalkablePoint) points[this.x][this.y];
        if (!currentPoint.getWalkableNeighbours().isEmpty()){
			Optional<WalkablePoint> optionalMinSFNNeighbor = currentPoint.getWalkableNeighbours().stream().filter(
					a -> !a.isOccupied()
			).min(Comparator.comparingDouble(p -> p.getStaticField(targetExit)));

			if (optionalMinSFNNeighbor.isEmpty()) {
                nextPosition = currentPoint.getPosition();
				return;
			}

			WalkablePoint minSFNNeighbor = optionalMinSFNNeighbor.get();
			System.out.println("Minimal static field: "+minSFNNeighbor.getStaticField(targetExit));
			currentPoint.numOfPedestrians--;
			minSFNNeighbor.numOfPedestrians++;
			nextPosition = minSFNNeighbor.getPosition();
		}
    }

    @Override
    public void move() {
        this.x = nextPosition.x();
        this.y = nextPosition.y();
    }
}
