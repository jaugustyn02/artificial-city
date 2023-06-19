package org.example;

import org.example.cells.Drivable;
import org.example.cells.Lights;

import java.util.ArrayList;
import java.util.List;

public class LightsInitializer {
    private Board board;
    private List<Lights> lights = new ArrayList<>();
    private List<State> states = new ArrayList<>();
    public LightsInitializer(Board board) {
        this.board = board;
    }
    public void addLights(int x, int y){
        Lights l = new Lights(x,y);
        board.lights.add(l);

        Point p = board.getPointAt(x,y);
        l.setPoint(p);
        if(p instanceof Drivable drivablePoint){
            drivablePoint.setLightsController(l);
        }
        lights.add(l);
    }

    public void addState(State state) {
        states.add(state);
    }

    public void commit() {
        LightsCrossingController controller = new LightsCrossingController();
        controller.initialize(lights, states);
        board.lightsCrossingControllers.add(controller);

        lights = new ArrayList<>();
        states = new ArrayList<>();
    }
}
