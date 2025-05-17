package com.roman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class IndexGenerator {
    public String createFileIndex(File textFile) throws FileNotFoundException {
        // a Map where they key is a word and the value is the list of the line numbers where this word was found
        Map<String, List<Integer>> wordsFound = new TreeMap<>();
        Scanner myReader = new Scanner(textFile);
        try {
            // line number
            int lineN = 0;
            while (myReader.hasNextLine()) {
                lineN++;
                String line = myReader.nextLine();
                String[] words = line.split("\\s+");
                int finalLineN = lineN;
                Arrays.stream(words).filter(word -> word.length() > 5).forEach(word ->
                        wordsFound.computeIfAbsent(word, k -> new ArrayList<>()).add(finalLineN));
            }
        } finally {
            myReader.close();
        }
        return wordsFound.entrySet().stream()
                .map(entry -> entry.getKey() + " " + entry.getValue().stream().map(Object::toString).collect(Collectors.joining(" ")))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
