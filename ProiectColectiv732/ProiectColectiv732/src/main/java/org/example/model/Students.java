package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;


import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
public class Students {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "firstName")
    @NotNull
    private String firstName;
    @Column(name = "lastName")
    @NotNull
    private String lastName;
    @Column(name = "cnp")
    @NotNull
    private String cnp;
    @Column(name = "birthDate")
    @NotNull
    private LocalDate birthDate;
    @Column(name = "studyYear")
    @NotNull
    private int studyYear;
    @Column(name = "studyLevel")
    @NotNull
    private String studyLevel;
    @Column(name = "fundingForm")
    @NotNull
    private String fundingForm;
    @Column(name = "graduatedHighSchool")
    @NotNull
    private String graduatedHighSchool;
}