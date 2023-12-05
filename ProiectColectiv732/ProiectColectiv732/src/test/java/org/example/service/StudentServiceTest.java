package org.example.service;

import org.example.model.Students;
import org.example.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testGetAllStudents() {
        Students student = createSampleStudent();
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        List<Students> students = studentService.getAllStudents();

        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        assertEquals(student, students.get(0));
    }

    @Test
    void testGetStudentById() {
        Students student = createSampleStudent();
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        Optional<Students> foundStudent = studentService.getStudentById(1);

        assertTrue(foundStudent.isPresent());
        assertEquals(student, foundStudent.get());
    }

    @Test
    void testUpdateStudent() {
        Students existingStudent = createSampleStudent();
        Students updatedStudent = createSampleStudent();

        when(studentRepository.findById(1)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Students.class))).thenReturn(updatedStudent);

        Optional<Students> result = studentService.updateStudent(1, updatedStudent);

        assertTrue(result.isPresent());
        assertEquals(updatedStudent, result.get());
    }

    @Test
    void testDeleteStudent() {
        Students student = createSampleStudent();
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        boolean deleted = studentService.deleteStudent(1);

        assertTrue(deleted);
        Mockito.verify(studentRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void testAddStudent() {
        Students student = createSampleStudent();
        when(studentRepository.save(any(Students.class))).thenReturn(student);

        Students addedStudent = studentService.addStudent("John", "Doe", "1234567890123", LocalDate.of(2000, 1, 1), 1, "Bachelor", "Self-funding", "High School");

        assertEquals(student, addedStudent);
    }

    private Students createSampleStudent() {
        return new Students(1, "John", "Doe", "1234567890123", LocalDate.of(2000, 1, 1), 1, "Bachelor", "Self-funding", "High School");
    }
}
