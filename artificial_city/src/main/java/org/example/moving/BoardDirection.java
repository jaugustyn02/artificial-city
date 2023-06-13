package org.example.moving;

import org.example.helpers.Vector2D;

public enum BoardDirection {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT;

    public Vector2D getVector(){
        return switch (this){
            case TOP -> new Vector2D(0,-1);
            case LEFT -> new Vector2D(-1,0);
            case BOTTOM -> new Vector2D(0,1);
            case RIGHT -> new Vector2D(1,0);
        };
    }

    public char getChar(){
        return switch (this){
            case TOP -> '↑';
            case LEFT -> '←';
            case BOTTOM -> '↓';
            case RIGHT -> '→';
        };
    }
}
