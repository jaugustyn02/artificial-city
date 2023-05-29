package org.example.moving;

import org.example.helpers.Vector2D;

public enum BoardDirection {
    TOP,
    TOP_LEFT,
    LEFT,
    BOTTOM_LEFT,
    BOTTOM,
    BOTTOM_RIGHT,
    RIGHT,
    TOP_RIGHT;

    public Vector2D getVector(){
        return switch (this){
            case TOP -> new Vector2D(0,-1);
            case TOP_LEFT -> new Vector2D(-1,-1);
            case LEFT -> new Vector2D(-1,0);
            case BOTTOM_LEFT -> new Vector2D(-1,1);
            case BOTTOM -> new Vector2D(0,1);
            case BOTTOM_RIGHT -> new Vector2D(1,1);
            case RIGHT -> new Vector2D(1,0);
            case TOP_RIGHT -> new Vector2D(1,-1);
        };
    }
}
