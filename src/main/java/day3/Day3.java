package day3;

import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 {

    private final int[][] playground;
    private final List<LocationEntry> locationEntryList;

    public static void main(String[] args) throws IOException {
        Day3 day3 = new Day3("src/main/resources/day3.txt");
        day3.printOverlaySquareInches();
        day3.printNonOverlayId();

    }

    private void printNonOverlayId() {
        for (LocationEntry locationEntry : locationEntryList) {
            boolean nonOverlappingClaimId = true;

            for (Integer x = locationEntry.delta.getKey(); x < locationEntry.delta.getKey() + locationEntry.length.getKey(); x++) {
                for (Integer y = locationEntry.delta.getValue(); y < locationEntry.delta.getValue() + locationEntry.length.getValue(); y++) {
                    if (playground[x][y] != locationEntry.objectNumber) {
                        nonOverlappingClaimId = false;
                    }
                }
            }
            if (nonOverlappingClaimId) {
                System.out.println("Non overlapping claimId: " + locationEntry.objectNumber);
                break;
            }
        }
    }

    private void printOverlaySquareInches() {
        int count = 0;
        for (int x = 0; x < playground.length; x++) {
            for (int y = 0; y < playground[x].length; y++) {
            if (playground[x][y] == -1) {
                count++;
            }
        }
        }
        System.out.println("Found so many duplicates: " + count);
    }

    public Day3(String filePath) throws IOException {
        locationEntryList = Files.lines(Paths.get(filePath)).map(LocationEntry::new).collect(Collectors.toList());
        Pair<Integer, Integer> max = findMax(locationEntryList);

        playground = new int[max.getKey()][max.getValue()];
        locationEntryList.forEach(this::addToStructure);
    }

    private Pair<Integer, Integer> findMax(List<LocationEntry> locationEntryList) {
        int maxX = 0;
        int maxY = 0;
        for (LocationEntry locationEntry : locationEntryList) {
            if (maxX < locationEntry.delta.getKey() + locationEntry.length.getKey()) {
                maxX = locationEntry.delta.getKey() + locationEntry.length.getKey();
            }
            if (maxY < locationEntry.delta.getValue() + locationEntry.length.getValue()) {
                maxY = locationEntry.delta.getValue() + locationEntry.length.getValue();
            }
        }
        return new Pair<>(maxX, maxY);
    }

    private void addToStructure(LocationEntry locationEntry) {
        Pair<Integer, Integer> length = locationEntry.length;
        Pair<Integer, Integer> delta = locationEntry.delta;
        for (Integer x = delta.getKey(); x < delta.getKey() + length.getKey(); x++) {
            for (int y = delta.getValue(); y < delta.getValue() + length.getValue(); y++) {
                if (playground[x][y] != -1 ) {
                    if (playground[x][y] == 0) {
                        playground[x][y] = locationEntry.objectNumber;
                    } else {
                        playground[x][y] = -1;
                    }
                }
            }
        }
    }

    private class LocationEntry {
        public int objectNumber;
        public Pair<Integer, Integer> delta;
        public Pair<Integer, Integer> length;


        public LocationEntry(String x) {
            String[] line = x.trim().split(" ");
            objectNumber = Integer.parseInt(line[0].substring(1));
            delta = parsePair(line[2].substring(0, line[2].indexOf(":")).split(","));
            length = parsePair(line[3].split("x"));
        }

        private Pair<Integer, Integer> parsePair(String[] obj) {
            return new Pair<>(Integer.parseInt(obj[0]), Integer.parseInt(obj[1]));
        }
    }
}
