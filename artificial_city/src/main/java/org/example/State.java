package org.example;

import org.example.cells.LightColor;

public record State(LightColor[] lightColors, int time) {

}
