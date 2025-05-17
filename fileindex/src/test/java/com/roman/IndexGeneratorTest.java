package com.roman;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class IndexGeneratorTest {
    @Test
    public void test1() throws IOException {
        testOneFile("./src/test/resources/inputs/test-file1.txt", "./src/test/resources/expected/result1.txt");
    }

    @Test
    public void test2() throws IOException {
        testOneFile("./src/test/resources/inputs/dorian.txt", "./src/test/resources/expected/dorian.txt");
    }

    private void testOneFile(String inputFilePath, String fileWithExpectedResultPath) throws IOException {
        IndexGenerator indexGenerator = new IndexGenerator();
        File inputFile = new File(inputFilePath);
        File fileWithExpectedResult = new File(fileWithExpectedResultPath);
        assertEquals(Files.readString(fileWithExpectedResult.toPath()), indexGenerator.createFileIndex(inputFile));
    }
}