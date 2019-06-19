package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataReader {

    private Map<String, BusLine> busLines;
    private Map<String, RouteCode> routeCodes;
    private List<BusPosition> busPositions;

    public DataReader() {
        this.busLines = new HashMap<>();
        this.routeCodes = new HashMap<>();

        this.busPositions = new ArrayList<>();

        loadBusLines();
        loadRouteCodes();
        loadBusPositions();
    }

    private void loadBusLines() {
        try {
            Files.lines(Paths.get("data/buslinesNew.txt"))
                    .forEach(r -> {
                        String[] splitLine = r.split(",");
                        busLines.put(splitLine[0], new BusLine(splitLine[0], splitLine[1], splitLine[2]));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRouteCodes() {
        try {
            Files.lines(Paths.get("data/RouteCodesNew.txt"))
                    .forEach(r -> {
                        String[] splitLine = r.split(",");
                        routeCodes.put(splitLine[1], new RouteCode(
                                splitLine[0],
                                splitLine[1],
                                splitLine[2],
                                splitLine[3]));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBusPositions() {
        try {
            Files.lines(Paths.get("data/busPositionsNew.txt"))
                    .forEach(r -> {
                        String[] splitLine = r.split(",");
                        busPositions.add(new BusPosition(
                                splitLine[0],
                                splitLine[1],
                                splitLine[2],
                                splitLine[3],
                                splitLine[4],
                                splitLine[5]));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, BusLine> getBusLines() {
        return busLines;
    }
    public Map<String, RouteCode> getRouteCodes() {
        return routeCodes;
    }
    public List<BusPosition> getBusPositions() { return busPositions; }

}
