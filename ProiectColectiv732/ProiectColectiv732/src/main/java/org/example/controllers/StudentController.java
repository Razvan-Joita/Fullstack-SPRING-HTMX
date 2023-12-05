package org.example.controllers;


import jakarta.persistence.EntityNotFoundException;
import org.example.model.Students;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Students> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Students> getStudentById(@PathVariable Integer id) {
        Students student = studentService.getStudentById(id).orElse(null);
        return (student != null) ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();

    }

    @PostMapping("")
    public ResponseEntity<?> addStudent(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String cnp,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam int studyYear,
            @RequestParam String studyLevel,
            @RequestParam String fundingForm,
            @RequestParam String graduatedHighSchool) {

        Students addedStudent = studentService.addStudent(
                firstName, lastName, cnp, birthDate, studyYear, studyLevel, fundingForm, graduatedHighSchool);

        return ResponseEntity.status(HttpStatus.CREATED).body(addedStudent);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Optional<Students>> updateStudent(@PathVariable Integer id, @RequestBody Students student) {
        Optional<Students> updatedStudent = studentService.updateStudent(id, student);

        if (updatedStudent.isPresent()) {
            return ResponseEntity.ok(updatedStudent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/modificareStudent")
    public String getModificareStudentPage() {
        return "modificareStudent";
    }

}
