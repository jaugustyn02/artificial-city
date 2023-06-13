package org.example;

import org.example.cells.*;
import org.example.iterable.IterablePoint;

import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.awt.event.ComponentEvent.COMPONENT_RESIZED;

public class FileHandler {
    private static final String resourcesPath = "src/main/resources/maps/";
    public static final String defaultFileName = "default.csv";
    public Board board;

    public FileHandler(Board board){
        this.board = board;
    }

    public static String[] getFilenamesToLoad() {
        File[] files = new File(resourcesPath).listFiles();
        if(files == null){
            return new String[]{};
        }
        return  Stream.of(files)
                .filter(f->!f.isDirectory())
                .map(File::getName)
                .toArray(String[]::new);
    }

    public void saveMap(Point[][] points){
        this.saveMap(points, defaultFileName);
    }

    public void saveMap(Point[][] points, String fileName){
        int length = points.length;
        int height = points[0].length;
        FileWriter fw;
        try {
            fw = new FileWriter(resourcesPath + fileName);

            fw.write(length + ";" + height + "\n");

            for (Point[] points_row : points) {
                for (Point point: points_row) {
                    if (point instanceof Drivable drivable){
                        fw.write(
                                point.type.ordinal()+":"+
                                Arrays.toString(drivable.getDrivableChances()).replaceAll("\\s", "")+";"
                        );
                    }
                    else {
                        fw.write(point.type.ordinal() + ";");
                    }
                }
                fw.write("\n");
            }

            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] loadSize(){
        return loadSize(defaultFileName);
    }

    public int[] loadSize(String fileName){
        int[] size = new int[2];
        size[0] = 1024;
        size[1] = 768;
        try (BufferedReader br = new BufferedReader(new FileReader(resourcesPath + fileName))) {
            String[] dimensions = br.readLine().split(";");
            int length = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);

            size[0] = length;
            size[1] = height;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public void loadMap(int[] size){
        loadMap(size, defaultFileName);
    }
    public void loadMap(int[] size, String fileName){
        try (BufferedReader br = new BufferedReader(new FileReader(resourcesPath + fileName))) {
            String[] dimensions = br.readLine().split(";");

//            int width = Integer.parseInt(dimensions[0]) * board.size;
//            int height = Integer.parseInt(dimensions[1]) * board.size;
//            board.setSize(width, height);
//            board.componentResized(null);

            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] points = line.split(";");
                for (int j = 0; j < size[1]; j++) {
                    if (i < size[0]) {
                        if (j < points.length) {
                            String[] values = points[j].split(":");
                            board.editType = CellType.values()[Integer.parseInt(values[0])];
                            if (board.editType.getObject() instanceof Drivable){
                                String[] str = values[1].replaceAll("[\\[\\]]", "").split(",");
                                
                                for(int i=0; i<)
                            }
//                            board.setCell(i, j);
                        }
                        else {
                            board.editType = CellType.NOT_SPECIFIED;
                            board.setCell(i, j);
                        }
                    }
                }
                i++;
            }
            for (int x = i;x < size[0]; x++){
                for (int y = 0; y < size[1]; y++){
                    board.editType = CellType.NOT_SPECIFIED;
                    board.setCell(x, y);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Lights l1 = new Lights(65,19);
        board.lights.add(l1);

        l1.addPoint(board.getPointAt(65,20));
        if(board.getPointAt(65,20) instanceof Drivable p){
            p.setLightsController(l1);
        }

        l1.addPoint(board.getPointAt(65,21));
        if(board.getPointAt(65,21) instanceof Drivable p){
            p.setLightsController(l1);
        }

        l1.addPoint(board.getPointAt(65,22));
        if(board.getPointAt(65,22) instanceof Drivable p){
            p.setLightsController(l1);
        }

        Lights l2 = new Lights(58,30);
        board.lights.add(l2);

        l2.addPoint(board.getPointAt(58,29));
        if(board.getPointAt(58,29) instanceof Drivable p){
            p.setLightsController(l2);
        }

        l2.addPoint(board.getPointAt(58,28));
        if(board.getPointAt(58,28) instanceof Drivable p){
            p.setLightsController(l2);
        }

        l2.addPoint(board.getPointAt(58,27));
        if(board.getPointAt(58,27) instanceof Drivable p){
            p.setLightsController(l2);
        }

        Lights l3 = new Lights(59,16);
        board.lights.add(l3);

        l3.addPoint(board.getPointAt(60,16));
        if(board.getPointAt(60,16) instanceof Drivable p){
            p.setLightsController(l3);
        }

        l3.addPoint(board.getPointAt(61,16));
        if(board.getPointAt(61,16) instanceof Drivable p){
            p.setLightsController(l3);
        }

        Lights l4 = new Lights(64,33);
        board.lights.add(l4);

        l4.addPoint(board.getPointAt(63,33));
        if(board.getPointAt(63,33) instanceof Drivable p){
            p.setLightsController(l4);
        }

        l4.addPoint(board.getPointAt(62,33));
        if(board.getPointAt(62,33) instanceof Drivable p){
            p.setLightsController(l4);
        }

        LightsCrossingController controller = new LightsCrossingController();
        List<Lights> lights = new ArrayList<>();
        lights.add(l1);
        lights.add(l2);
        lights.add(l3);
        lights.add(l4);

        List<State> states = new ArrayList<>();
        states.add(new State(new LightColor[]{LightColor.GREEN, LightColor.GREEN,LightColor.RED, LightColor.RED}, 50));
        states.add(new State(new LightColor[]{LightColor.RED, LightColor.RED,LightColor.RED, LightColor.RED}, 5));
        states.add(new State(new LightColor[]{LightColor.RED,LightColor.RED,LightColor.GREEN, LightColor.GREEN}, 50));
        states.add(new State(new LightColor[]{LightColor.RED,LightColor.RED,LightColor.RED, LightColor.RED}, 5));

        controller.initialize(lights, states);
        board.lightsCrossingControllers.add(controller);

//        return points;
    }
}
