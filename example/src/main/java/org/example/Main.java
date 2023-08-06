package org.example;

import org.example.dao.StudentDao;
import org.example.entity.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        StudentDao studentDao = new StudentDao();
        Student student = new Student("Ramesh", "Fadatare", "rameshfadatare@javaguides.com");
        studentDao.saveStudent(student);

        List<Student> students = studentDao.getStudents();
        students.forEach(s -> System.out.println(s.getFirstName()));
    }
}