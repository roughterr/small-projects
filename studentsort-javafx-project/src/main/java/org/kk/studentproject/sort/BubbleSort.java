package org.kk.studentproject.sort;

import org.kk.studentproject.Student;

import java.util.List;

/**
 * Sorts students using bubble sort.
 */
public class BubbleSort<T extends Student> implements StudentSortI {
    @Override
    public void sort(List<Student> list) {
        int i, j;
        for (i = 0; i < list.size() - 1; i++)
            // Last i elements are already in place
            for (j = 0; j < list.size() - i - 1; j++) {
                Student firstStudent = list.get(j);
                Student secondStudent = list.get(j + 1);
                if (firstStudent.getPerformance() > secondStudent.getPerformance()) {
                    list.set(j + 1, firstStudent);
                    list.set(j, secondStudent);
                }
            }
    }
}
