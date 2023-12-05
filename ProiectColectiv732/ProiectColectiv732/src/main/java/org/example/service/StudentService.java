package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.model.Students;
import org.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    private static final int CNP_LENGTH = 13;

    private boolean isValidCnpLength(String cnp) {
        return cnp != null && cnp.length() == CNP_LENGTH;
    }

    public List<Students> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Students> getStudentById(Integer id) {
        return studentRepository.findById(id);
    }

    public Optional<Students> updateStudent(int studentId, Students updatedStudent) {
        Optional<Students> existingStudent = studentRepository.findById(studentId);

        if (existingStudent.isPresent()) {
            Students savedStudent = existingStudent.get();

            savedStudent.setFirstName(updatedStudent.getFirstName());
            savedStudent.setLastName(updatedStudent.getLastName());
            savedStudent.setCnp(updatedStudent.getCnp());
            savedStudent.setBirthDate(updatedStudent.getBirthDate());
            savedStudent.setStudyYear(updatedStudent.getStudyYear());
            savedStudent.setStudyLevel(updatedStudent.getStudyLevel());
            savedStudent.setFundingForm(updatedStudent.getFundingForm());
            savedStudent.setGraduatedHighSchool(updatedStudent.getGraduatedHighSchool());

            Students updatedSavedStudent = studentRepository.save(savedStudent);

            return Optional.of(updatedSavedStudent);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteStudent(Integer id) {
        Optional<Students> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            studentRepository.deleteById(id);
            return true;
        } else {
            throw new EntityNotFoundException("Student with ID " + id + " not found");
        }
    }

    public Students addStudent(
            String firstName,
            String lastName,
            String cnp,
            LocalDate birthDate,
            int studyYear,
            String studyLevel,
            String fundingForm,
            String graduatedHighSchool) {

        Students student = new Students();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setCnp(cnp);
        student.setBirthDate(birthDate);
        student.setStudyYear(studyYear);
        student.setStudyLevel(studyLevel);
        student.setFundingForm(fundingForm);
        student.setGraduatedHighSchool(graduatedHighSchool);

        if (isValidCnpLength(student.getCnp())) {
            return studentRepository.save(student);
        } else {
            throw new IllegalArgumentException("Invalid CNP length. CNP must be exactly 13 digits.");
        }
    }

}
