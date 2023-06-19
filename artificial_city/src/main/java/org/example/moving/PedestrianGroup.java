package org.example.moving;

import org.example.MovingObject;
import org.example.Point;
import org.example.cells.CellType;

import java.util.ArrayList;
import java.util.List;

public class PedestrianGroup extends MovingObject {
    List<Pedestrian> pedestrians = new ArrayList<>();

    public PedestrianGroup(){
        super(CellType.PEDESTRIAN);
    }

    public void addPedestrian(Pedestrian pedestrian){
        pedestrians.add(pedestrian);
    }

    public List<Pedestrian> getPedestrians(){
        return pedestrians;
    }

    @Override
    public void iterate(Point[][] points, MovingObject[][] movingObjects) {
        for (Pedestrian pedestrian: pedestrians){
            pedestrian.iterate(points, movingObjects);
        }
    }

    @Override
    public void move() {
        for (Pedestrian pedestrian: pedestrians){
            pedestrian.move();
        }
    }

    @Override
    public String getInfo(){
        return super.getInfo() + ", numOfPedestrians: " + pedestrians.size();
    }
}
