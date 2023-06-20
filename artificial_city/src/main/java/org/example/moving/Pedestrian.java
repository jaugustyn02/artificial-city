package org.example.moving;

import org.example.Config;
import org.example.MovingObject;
import org.example.Point;
import org.example.cells.CellType;
import org.example.cells.Crossing;
import org.example.cells.WalkablePoint;
import org.example.iterable.PedestrianExit;

import java.util.Comparator;
import java.util.Optional;

public class Pedestrian extends MovingObject {
    private PedestrianExit targetExit;
    private int iterationNum = -1;

    public Pedestrian() {
        super(CellType.PEDESTRIAN);
    }

    public void setTargetExit(PedestrianExit exit){
        targetExit = exit;
    }

    @Override
    public void iterate(Point[][] points, MovingObject[][] movingObjects) {
        WalkablePoint currentPoint = (WalkablePoint) points[this.x][this.y];
        ++iterationNum;
        if (iterationNum % Config.pedestriansMoveTime != 0){
            nextPosition = currentPoint.getPosition();
            return;
        }

        if (!currentPoint.getWalkableNeighbours().isEmpty()){
			Optional<WalkablePoint> optionalMinSFNNeighbor = currentPoint.getWalkableNeighbours().stream().filter(
					a -> !a.isOccupied()
			).min(Comparator.comparingDouble(p -> p.getStaticField(targetExit)));

			if (optionalMinSFNNeighbor.isEmpty()){
                nextPosition = currentPoint.getPosition();
				return;
			}

            WalkablePoint minSFNNeighbor = optionalMinSFNNeighbor.get();
            if (currentPoint.getStaticField(targetExit) <= minSFNNeighbor.getStaticField(targetExit) ||
                movingObjects[minSFNNeighbor.getPosition().x()][minSFNNeighbor.getPosition().y()] instanceof Car){
                nextPosition = currentPoint.getPosition();
                return;
            }

			currentPoint.decrementNumOfPedestrians();
			minSFNNeighbor.incrementNumOfPedestrians();
			nextPosition = minSFNNeighbor.getPosition();

            if (!(currentPoint instanceof Crossing) && minSFNNeighbor instanceof Crossing crossing){
                crossing.blockNeighbourCrossing();
            }
            else if (currentPoint instanceof Crossing crossing && !(minSFNNeighbor instanceof Crossing)){
                crossing.unblockNeighbourCrossing();
            }
		}
    }

    @Override
    public void move() {
        this.x = nextPosition.x();
        this.y = nextPosition.y();
    }

    @Override
    public String getInfo(){
        return super.getInfo() + ", ExitID: " + targetExit.EXIT_ID;
    }
}
