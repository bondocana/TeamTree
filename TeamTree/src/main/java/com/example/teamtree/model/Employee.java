package com.example.teamtree.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @Column(unique = true)
    private String email;

    @ManyToOne
    @JsonBackReference("employee-department") // <--- Nume unic
    private Department department;

    @ManyToOne
    @JsonBackReference("employee-position")   // <--- Nume unic
    private Position position;

    @ManyToOne
    @JsonBackReference("employee-contract")   // <--- Nume unic
    private Contract contract;

    @ManyToOne
    @JsonBackReference("employee-role")       // <--- Nume unic
    private Role role;

    @ManyToOne
    @JsonBackReference("employee-manager")    // <--- Nume unic
    private Employee manager;

    // Adaugă cascade = CascadeType.ALL și orphanRemoval = true
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("employee-salary")
    private List<Salary> salaries;

    private LocalDate hireDate;
}