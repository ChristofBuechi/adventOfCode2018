package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day2 {

    private String filePath;
    private int twice;
    private int tripple;
    private String prototype;


    public Day2(String filePath) {
        this.filePath = filePath;
    }

    public static void main(String... args) throws IOException {
        System.out.println("start Day2");

        Day2 day2 = new Day2("src/main/resources/day2.txt");
        day2.readInput();
        day2.printResults();
    }

    private void readInput() throws IOException {

        List<String> lines = Files.lines(Paths.get(filePath)).sorted().collect(Collectors.toList());
        lines.forEach(line -> {
            containsLetterMultipleTimes(line);
            findPrototype(line, lines);
        });
    }

    private void containsLetterMultipleTimes(String line) {
        Map<Character, Integer> map = new HashMap<>();
        for (Character c : line.toCharArray()) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            } else {
                map.put(c, 1);
            }
        }

        long containsDupplicates = map.entrySet().stream().filter(entry -> entry.getValue() == 2).limit(1).count();
        twice += containsDupplicates;

        long containsTripple = map.entrySet().stream().filter(entry -> entry.getValue() == 3).limit(1).count();
        tripple += containsTripple;
    }

    private void findPrototype(String toSearch, List<String> lines) {
        for (String input : lines) {
            if (prototype == null && exactOneDifference(toSearch, input)) {
                prototype = removeDifferentCharacter(toSearch, input);
            }
        }
    }

    private boolean exactOneDifference(String toSearch, String input) {
        int mismatches = 0;
        char[] toSearchCharArray = toSearch.toCharArray();
        char[] inputCharArray = input.toCharArray();
        for (int i = 0; i < toSearchCharArray.length; i++) {
            if (toSearchCharArray[i] != inputCharArray[i])
                mismatches++;
        }
        return mismatches == 1;
    }

    private String removeDifferentCharacter(String toSearch, String input) {
        StringBuilder result = new StringBuilder();
        char[] toSearchCharArray = toSearch.toCharArray();
        char[] inputCharArray = input.toCharArray();
        for (int i = 0; i < toSearchCharArray.length; i++) {
            if (toSearchCharArray[i] == inputCharArray[i]) {
                result.append(toSearchCharArray[i]);
            }
        }
        return result.toString();
    }

    private void printResults() {
        System.out.println("Result is: " + twice * tripple);
        System.out.println("Prototype is: " + prototype);
    }

}
