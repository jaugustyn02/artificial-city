package org.example.iterable;

import org.example.moving.BoardDirection;

import java.util.HashMap;
import java.util.Map;

public class DrivingPathChances {
    Map<BoardDirection, Map<BoardDirection, Integer>> pathChancesFromDirection;

    public DrivingPathChances(){
        pathChancesFromDirection = new HashMap<>();
        for (BoardDirection from: BoardDirection.values()){
            Map<BoardDirection, Integer> chancesTo = new HashMap<>();
            for (BoardDirection to: BoardDirection.values()){
                chancesTo.put(to, 0);
            }
            pathChancesFromDirection.put(from, chancesTo);
        }
    }

    public DrivingPathChances(DrivingPathChances chances){
        pathChancesFromDirection = new HashMap<>();
        for (BoardDirection from: BoardDirection.values()){
            Map<BoardDirection, Integer> chancesTo = new HashMap<>();
            for (BoardDirection to: BoardDirection.values()){
                chancesTo.put(to, chances.getChancesFromTo(from, to));
            }
            pathChancesFromDirection.put(from, chancesTo);
        }
    }

    public DrivingPathChances(String str){
        pathChancesFromDirection = new HashMap<>();
        String[] values = str.replaceAll("[\\[\\]]", "").split(",");
        int i=0;
        for (BoardDirection from: BoardDirection.values()){
            Map<BoardDirection, Integer> chancesTo = new HashMap<>();
            for (BoardDirection to: BoardDirection.values()){
                chancesTo.put(to, Integer.parseInt(values[i]));
                ++i;
            }
            pathChancesFromDirection.put(from, chancesTo);
        }
    }

    public void setChanceFromTo(BoardDirection from, BoardDirection to, int chance){
        (pathChancesFromDirection.get(from)).put(to, chance);
    }

    public Map<BoardDirection, Integer> getChancesFrom(BoardDirection direction){
        return pathChancesFromDirection.get(direction);
    }

    public int getChancesFromTo(BoardDirection from, BoardDirection to){
        return pathChancesFromDirection.get(from).get(to);
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("[");
        for (BoardDirection from: BoardDirection.values()){
            for (BoardDirection to: BoardDirection.values()){
                str.append(pathChancesFromDirection.get(from).get(to).toString()).append(",");
            }
        }
        str.replace(str.lastIndexOf(","), str.lastIndexOf(",")+1, "]");
        return str.toString();
    }
}
