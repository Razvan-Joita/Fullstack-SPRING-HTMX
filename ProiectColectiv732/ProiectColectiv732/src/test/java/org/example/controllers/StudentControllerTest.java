package org.example.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.example.model.Students;
import org.example.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStudents() {
        List<Students> studentsList = Arrays.asList(new Students(), new Students());
        when(studentService.getAllStudents()).thenReturn(studentsList);

        assertEquals(studentsList, studentController.getAllStudents());
    }

    @Test
    void getStudentById() {
        int studentId = 1;
        Students student = new Students();
        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(student));

        ResponseEntity<Students> responseEntity = studentController.getStudentById(studentId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(student, responseEntity.getBody());
    }

    @Test
    void getStudentByIdNotFound() {
        int nonExistingStudentId = 999;
        when(studentService.getStudentById(nonExistingStudentId)).thenReturn(Optional.empty());

        ResponseEntity<Students> responseEntity = studentController.getStudentById(nonExistingStudentId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void addStudent() {
        Students student = new Students();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setCnp("1234567890123");

        when(studentService.addStudent(anyString(), anyString(), anyString(), any(LocalDate.class), anyInt(), anyString(), anyString(), anyString())).thenReturn(student);

        ResponseEntity<?> responseEntity = studentController.addStudent("John", "Doe", "1234567890123", LocalDate.of(2000, 1, 1), 1, "Bachelor", "Self-funding", "High School");

        verify(studentService, times(1)).addStudent(anyString(), anyString(), anyString(), any(LocalDate.class), anyInt(), anyString(), anyString(), anyString());

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(student, responseEntity.getBody());
    }


    @Test
    void updateStudent() {
        int studentId = 3;
        Students updatedStudent = new Students();
        Optional<Students> existingStudent = Optional.of(new Students());

        when(studentService.updateStudent(studentId, updatedStudent)).thenReturn(existingStudent);

        ResponseEntity<Optional<Students>> responseEntity = studentController.updateStudent(studentId, updatedStudent);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(existingStudent, responseEntity.getBody());
    }

    @Test
    void updateStudentNotFound() {
        int nonExistingStudentId = 999;
        Students updatedStudent = new Students();

        when(studentService.updateStudent(nonExistingStudentId, updatedStudent)).thenReturn(Optional.empty());

        ResponseEntity<Optional<Students>> responseEntity = studentController.updateStudent(nonExistingStudentId, updatedStudent);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void deleteStudent() {
        int studentId = 1;

        ResponseEntity<?> responseEntity = studentController.deleteStudent(studentId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(studentService, times(1)).deleteStudent(studentId);
    }

    @Test
    void deleteStudentNotFound() {
        int nonExistingStudentId = 999;

        when(studentService.deleteStudent(nonExistingStudentId)).thenThrow(new EntityNotFoundException("Student not found"));

        ResponseEntity<?> responseEntity = studentController.deleteStudent(nonExistingStudentId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Student not found", responseEntity.getBody());
    }

}