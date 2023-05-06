package org.kk.studentproject;

import java.util.Objects;

public class Student {
    private String name;
    private Float performance;

    public Student(String name, Float performance ) {
        this.name = name;
        this.performance = performance;
    }

    public String getName() {
        return name;
    }

    public Float getPerformance() {
        return performance;
    }

    public String toCSVString() {
        return name + "," + performance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", performance=" + performance +
                '}';
    }
}
