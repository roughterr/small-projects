package org.kk.studentproject.sort;

import org.kk.studentproject.Student;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for sorting students.
 */
public class StudentSort {
    public static final Map<SortingAlgorithm, StudentSortI> SORT_ALGO_MAP = Map.of(SortingAlgorithm.BUBBLE, new BubbleSort());

    /**
     * Sorts a list of students.
     *
     * @param students  a list of students
     * @param algorithm sorting algorithm
     * @return sorting time (in nanoseconds)
     */
    public long sort(List<Student> students, SortingAlgorithm algorithm) {
        StudentSortI studentSortI = SORT_ALGO_MAP.get(algorithm);
        if (studentSortI == null) {
            throw new RuntimeException("Sorting algorithm is not supported yet");
        }
        Instant start = Instant.now();
        studentSortI.sort(students);
        Instant end = Instant.now();
        return Duration.between(start, end).toNanos();
    }
}
