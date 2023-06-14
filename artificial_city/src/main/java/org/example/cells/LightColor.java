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
}
