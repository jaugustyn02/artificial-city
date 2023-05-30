package org.example;

import org.example.cells.Lights;

import java.util.ArrayList;
import java.util.List;

public class LightsCrossingController {
    private List<Lights> lightsList = new ArrayList<>();
    private List<State> statesList = new ArrayList<>();
    private int timeToChange = 0;
    private int currentState = 0;


    public void initialize(List<Lights> lightsList, List<State> stateList){
        this.lightsList = lightsList;
        this.statesList = stateList;
        this.currentState = stateList.size()-1;
    }

    public void iterate(){

        if(timeToChange == 0){
            currentState++;
            currentState = currentState%statesList.size();
            timeToChange = statesList.get(currentState).time();

            State state = statesList.get(currentState);
            for(int i=0; i<lightsList.size(); i++){
                lightsList.get(i).setColor(state.lightColors()[i]);
            }
        }


        if(timeToChange != 0){
            timeToChange--;
        }
    }

}
