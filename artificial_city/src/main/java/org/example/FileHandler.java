package org.example;

import org.example.cells.*;
import org.example.iterable.DrivingPathChances;


import java.io.*;
import java.util.*;
import java.util.stream.Stream;

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
                                point.type.ordinal()+":" + drivable.getDrivingPathChances().toString() + ";"
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
    public void loadMap(String fileName){
        if (Objects.equals(fileName, "")){
            fileName = defaultFileName;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(resourcesPath + fileName))) {
            br.readLine(); // read size at 1st line

            int length = board.getPointsLength();
            int height = board.getPointsHeight();

            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if(line.equals("LIGHTS:")){
                    readLightsConfig(br);
                    break;
                }
                String[] points = line.split(";");
                for (int j = 0; j < height; j++) {
                    if (i < length) {
                        if (j < points.length) {
                            String[] values = points[j].split(":");
                            board.editType = CellType.values()[Integer.parseInt(values[0])];
                            if (board.editType.getObject() instanceof Drivable){
                                board.editChances = new DrivingPathChances(values[1]);
                            }
                            board.setCell(i, j);
                        }
                        else {
                            board.editType = CellType.NOT_SPECIFIED;
                            board.setCell(i, j);
                        }
                    }
                }
                i++;
            }
            for (int x = i;x < length; x++){
                for (int y = 0; y < height; y++){
                    board.editType = CellType.NOT_SPECIFIED;
                    board.setCell(x, y);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        board.editType = CellType.SELECT;
    }
    private void readLightsConfig(BufferedReader br) throws IOException{
        LightsInitializer initializer = new LightsInitializer(board);
        String line = br.readLine();
        String[] lightsS = line.split(";");
        for(String lightS : lightsS){
            String[] cordinatesS = lightS.split(",");
            String regex = "\\D";
            String xS = cordinatesS[0].replaceAll(regex, "");
            String yS = cordinatesS[1].replaceAll(regex, "");
            int x = Integer.parseInt(xS);
            int y = Integer.parseInt(yS);
            initializer.addLights(x,y);
        }
        while ((line = br.readLine()) != null){
            if(line.equals("LIGHTS:")){
                initializer.commit();
                readLightsConfig(br);
                return;
            }
            String[] statesS = line.split(",");
            String[] lightsColorsS = statesS[0].split(" ");
            int time = Integer.parseInt(statesS[1].strip());
            LightColor[] lightColors = Arrays.stream(lightsColorsS).sequential()
                    .map(c->c.replaceAll("(\\[|\\])", ""))
                    .map(c-> switch (c.strip()){
                        case "R" -> LightColor.RED;
                        case "Y" -> LightColor.YELLOW;
                        case "G" -> LightColor.GREEN;
                        default -> null;
                    }).toArray(LightColor[]::new);

            State state = new State(lightColors, time);
            initializer.addState(state);
        }


        initializer.commit();
    }

}
