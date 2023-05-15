package org.example;

import org.example.cells.CellType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHandler {
    private static final String resourcesPath = "src/main/resources/maps/";
    public static final String defaultFileName = "default.csv";

    public FileHandler(){}

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
        FileWriter fw = null;
        try {
            fw = new FileWriter(resourcesPath + fileName);

            fw.write(length + ";" + height + "\n");

            for (Point[] points_row : points) {
                for (Point point: points_row) {
                    fw.write(point.type.ordinal() + ";");
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

    public Point[][] loadMap(int[] size){
        return loadMap(size, defaultFileName);
    }
    public Point[][] loadMap(int[] size, String fileName){
        Point[][] points;
        try (BufferedReader br = new BufferedReader(new FileReader(resourcesPath + fileName))) {
            String[] dimensions = br.readLine().split(";");

//            int length = Integer.parseInt(dimensions[0]);
//            int height = Integer.parseInt(dimensions[1]);

            points = new Point[size[0]][size[1]];

            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] types = line.split(";");
                for (int j = 0; j < size[1]; j++) {
                    if (i < size[0]) {
                        if (j < types.length) {
                            points[i][j] = CellType.values()[Integer.parseInt(types[j])].getObject();
                        }
                        else {
                            points[i][j] = CellType.NOT_SPECIFIED.getObject();
                        }
                    }
                }
                i++;
            }
            for (int x = i;x < size[0]; x++){
                for (int y = 0; y < size[1]; y++){
                    points[x][y] = CellType.NOT_SPECIFIED.getObject();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            points = new Point[][]{{CellType.NOT_SPECIFIED.getObject()}};
        }
        return points;
    }
}
