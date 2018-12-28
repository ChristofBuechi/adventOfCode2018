package day5;

import javafx.util.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class Day5 {


    private final File file;

    public static void main(String[] args) throws IOException {
        Day5 day5 = new Day5("src/main/resources/day5.txt");
        day5.reactEndless();
        day5.reactMostBeneficial();
    }

    public Day5(String filePath) {
        file = Paths.get(filePath).toFile();
    }

    private void reactEndless() throws IOException {
        Pair<ByteArrayOutputStream, Integer> res = loop(new FileInputStream(file));
        System.out.println("resultsize: " + res.getKey().toByteArray().length);
    }

    private void reactMostBeneficial() throws IOException {

        String inputString = Files
                .lines(file.toPath())
                .map(String::toLowerCase).findFirst().get();

        Map<Character, Integer> map =
                inputString.chars().distinct().mapToObj(value -> (char) value).collect(Collectors.toMap(c -> c, c -> 0));

        for (Character character : map.keySet()) {
            String input = Files.lines(file.toPath()).findFirst().get();
            String inputFiltered = input.replaceAll(character.toString(), "").replaceAll(character.toString().toUpperCase(), "");


            Pair<ByteArrayOutputStream, Integer> loop = loop(new ByteArrayInputStream(inputFiltered.getBytes(StandardCharsets.UTF_8)));
            map.put(character, loop.getKey().toByteArray().length);
        }

        Map.Entry<Character, Integer> shortestPolymer = map.entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).get();
        System.out.println("The shortest polymer length is: " + shortestPolymer.getValue() + ", by removing character: " + shortestPolymer.getKey().toString());
    }

    public Pair<ByteArrayOutputStream, Integer> loop(InputStream inputStream) throws IOException {
        Pair<ByteArrayOutputStream, Integer> res = reacting(inputStream);
        if (res.getValue() > 0) {
            return loop(new ByteArrayInputStream(res.getKey().toByteArray()));
        }
        return res;
    }

    public Pair<ByteArrayOutputStream, Integer> reacting(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int filteredValues = 0;

        char lastCharacter = (char) 0;
        for (int i = 0; (i = inputStream.read()) != -1; ) {
            char character = (char) i;
            if (lastCharacter != 0) {
                //check
                if (lastCharacter + 32 == character || lastCharacter - 32 == character) {
                    lastCharacter = (char) 0;
                    filteredValues++;
                } else {
                    outputStream.write(lastCharacter);
                    lastCharacter = character;
                }
            } else {
                lastCharacter = character;
            }
        }
        outputStream.write(lastCharacter);
        return new Pair<>(outputStream, filteredValues);
    }
}
