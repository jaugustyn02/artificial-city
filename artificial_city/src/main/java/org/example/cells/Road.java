package org.example.cells;

import org.example.Point;
import org.example.moving.BoardDirection;
import org.example.moving.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Road extends Point implements Drivable {
//    private List<BoardDirection>  availableDirections = new ArrayList<>();
    private int[] directionChances = new int[BoardDirection.values().length];
    private List<BoardDirection> availableOvertakingDirections = new ArrayList<>();

    private Lights lightsController = null;
    public Road(){
        super(CellType.ROAD);
    }

    @Override
    public boolean canDriveThrough() {
        if(lightsController == null){
            return true;
        }

        if(lightsController.getLightColor() == LightColor.GREEN){
            return true;
        }
        return false;
    }

    @Override
    public void setLightsController(Lights l) {
        lightsController = l;
    }

    @Override
    public List<BoardDirection> getAvailableDirections() {
        return null;
    }

    @Override
    public BoardDirection getDriveDirection() {
        Random rand = new Random();
        int percent = rand.nextInt(100);
        int index = 0;
        for (int chance: directionChances) {
            if (percent < chance) {
                System.out.println("Returns: "+BoardDirection.values()[index]);
                return BoardDirection.values()[index];
            }
            percent -= chance;
            ++index;
        }
        return null; // no available direction or error
    }

    @Override
    public void setDirectionChances(int[] chances){
        System.arraycopy(chances, 0, directionChances, 0, BoardDirection.values().length);
        System.out.println(Arrays.toString(directionChances));
    }

    @Override
    public int[] getDrivableChances(){
        return directionChances;
    }
}
