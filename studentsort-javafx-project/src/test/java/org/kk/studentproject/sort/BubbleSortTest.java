package org.kk.studentproject.sort;

import org.junit.jupiter.api.Test;
import org.kk.studentproject.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BubbleSortTest {
    private static Student STUDENT1 = new Student("student1", 1.2f);
    private static Student STUDENT2 = new Student("student2", 1.4f);
    private static Student STUDENT3 = new Student("student3", 1.0f);
    private static Student STUDENT4 = new Student("student4", 2.0f);

    private static List<Student> STUDENTS_UNSORTED = List.of(STUDENT1, STUDENT2, STUDENT3, STUDENT4);
    private static List<Student> STUDENTS_SORTED = List.of(STUDENT3, STUDENT1, STUDENT2, STUDENT4);

    @Test
    public void testBubbleSort() {
        BubbleSort bubbleSort = new BubbleSort();
        List<Student> students = new ArrayList<>(STUDENTS_UNSORTED);
        bubbleSort.sort(students);
        assertEquals(STUDENTS_SORTED, students);
    }
}