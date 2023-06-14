package org.example.cells;

import org.example.MovingObject;
import org.example.Point;
import org.example.iterable.CarEntrance;
import org.example.iterable.CarExit;
import org.example.iterable.PedestrianEntrance;
import org.example.iterable.PedestrianExit;
import org.example.moving.BoardDirection;
import org.example.moving.Car;
import org.example.moving.Pedestrian;

import java.awt.*;


public enum CellType {
    SELECT,
    NOT_SPECIFIED,
    CAR,
    PEDESTRIAN,
    ROAD,
    SIDEWALK,
    CROSSING,
    LIGHTS,
    GRASS,
    TREE,
    BUILDING,
    PARKING,
    CAR_EXIT,
    CAR_ENTRANCE,
    PEDESTRIAN_EXIT,
    PEDESTRIAN_ENTRANCE;

    public  Color getColor(){
        return switch (this) {
            case CAR -> new Color(0, 0, 0);
            case PEDESTRIAN -> new Color(45, 84, 245);
            case ROAD -> new Color(185, 185, 185);
            case SIDEWALK -> new Color(128, 128, 128);
            case CROSSING -> new Color(241, 241, 19);
            case LIGHTS -> new Color(255, 0, 0);
            case GRASS -> new Color(51, 182, 0);
            case TREE -> new Color(16, 128, 0);
            case BUILDING -> new Color(141, 0, 11);
            case PARKING -> new Color(87, 87, 87);
            case CAR_EXIT -> new Color(202, 3, 250);
            case CAR_ENTRANCE -> new Color(66, 255, 227);
            case PEDESTRIAN_EXIT -> new Color(130, 0, 250);
            case PEDESTRIAN_ENTRANCE -> new Color(141, 255, 2);
            default -> new Color(255, 255, 255);
        };
    }

   public Point getObject(){
       return switch (this) {
           case SELECT -> null;
           case ROAD -> new Road();
           case SIDEWALK -> new Sidewalk();
           case CROSSING -> new Crossing();
//           case LIGHTS -> new Lights();
           case GRASS -> new Grass();
           case TREE -> new Tree();
           case BUILDING -> new Building();
           case PARKING -> new Parking();
           case CAR_EXIT -> new CarExit();
           case CAR_ENTRANCE -> new CarEntrance();
           case PEDESTRIAN_EXIT -> new PedestrianExit();
           case PEDESTRIAN_ENTRANCE -> new PedestrianEntrance();
           default -> new Point();
       };
   }

   public MovingObject getMovingObject(BoardDirection direction){
       return switch (this) {
           case CAR -> new Car(direction);
           case PEDESTRIAN -> new Pedestrian();
           default -> null;
       };
   }

    public MovingObject getMovingObject() {
        return switch (this) {
            case CAR -> new Car();
            case PEDESTRIAN -> new Pedestrian();
            default -> null;
        };
    }
}
