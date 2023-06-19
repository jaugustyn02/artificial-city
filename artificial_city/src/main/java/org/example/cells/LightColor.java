package org.example.cells;

import java.awt.*;

public enum LightColor {
    GREEN,
    YELLOW,
    RED;

    public Color getColor(){
        return switch (this)  {
            case GREEN -> Color.GREEN;
            case YELLOW -> Color.YELLOW;
            case RED -> Color.RED;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return "LightColor{" +
            switch (this) {
                case RED -> "RED";
                case GREEN -> "GREEN";
                case YELLOW -> "YELLOW";
            }
                    +"}";
    }
}
