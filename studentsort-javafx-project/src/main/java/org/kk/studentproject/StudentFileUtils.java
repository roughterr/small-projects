package org.kk.studentproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StudentFileUtils {
    public String studentListToStr(List<Student> students) {
        return students.stream().map(Student::toCSVString).collect(Collectors.joining("\n"));
    }

    public List<Student> studentFileToList(File file) {
        List<Student> students = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            try (scanner) {
                while (scanner.hasNextLine()) {
                    String data = scanner.nextLine();
                    Optional<Student> studentOptional = strToStudent(data);
                    studentOptional.ifPresent(students::add);
                }
            }
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        }
        return students;
    }

    /**
     * Converts a string to a Student object.
     * @param studentStr
     * @return
     */
    protected Optional<Student> strToStudent(String studentStr) {
        if (studentStr == null || studentStr.equals("")) {
            return Optional.empty();
        }
        String[] studentArr = studentStr.split(",");
        if (studentArr.length != 2) {
            System.out.println("Unexpected number of columns in CSV: " + studentArr.length);
            return Optional.empty();
        }
        String name = studentArr[0];
        String performance = studentArr[1];
        float performanceFloat;
        try {
            performanceFloat = Float.parseFloat(performance);
        } catch (NumberFormatException nfe) {
            System.out.println("Unexpected performance format value: " + performance);
            return Optional.empty();
        }
        return Optional.of(new Student(name, performanceFloat));
    }
}
